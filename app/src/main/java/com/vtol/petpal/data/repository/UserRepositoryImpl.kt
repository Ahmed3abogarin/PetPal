package com.vtol.petpal.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vtol.petpal.domain.model.user.ProviderInfo
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.util.AppStoragePaths
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : UserRepository {
    override fun getUser(): Flow<User> = callbackFlow {
        val currentUid = auth.currentUser?.uid
        if (currentUid == null) {
            close(IllegalStateException("User not found"))
            return@callbackFlow
        }

        val listener = firestore.collection(USERS_COLLECTION)
            .document(currentUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.toObject(User::class.java)?.let { trySend(it) }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateUserProfileImage(bytes: ByteArray): Result<String> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")
        val ref = storage.reference.child(
            AppStoragePaths.userProfileStoragePath(currentUid)
        )

        ref.putBytes(bytes).await()

        val downloadUrl = ref.downloadUrl.await().toString()
        firestore.collection(USERS_COLLECTION)
            .document(currentUid)
            .update("imgPath", downloadUrl)
            .await()

        downloadUrl
    }

    override suspend fun updateUsername(name: String): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")
        firestore.collection(USERS_COLLECTION)
            .document(currentUid)
            .update("name", name)
            .await()
    }

    override suspend fun updatePhoneNumber(phone: String): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid ?: throw Exception("User not found")

        firestore.collection(USERS_COLLECTION)
            .document(currentUid)
            .update("phoneNumber", phone)
            .await()
    }

    override suspend fun updatePassword(oldPw: String, newPw: String): Result<Unit> = runCatching {
        val user = auth.currentUser ?: throw Exception("User not found")

        // Re-authenticate first — Firebase requires this before sensitive changes
        val credential = EmailAuthProvider.getCredential(user.email!!, oldPw)
        user.reauthenticate(credential).await()

        // Now safe to update
        user.updatePassword(newPw).await()
    }

    override suspend fun deleteUserImage(): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")
        storage.reference.child(AppStoragePaths.userProfileStoragePath(currentUid)).delete().await()

        firestore.collection(USERS_COLLECTION)
            .document(currentUid)
            .update("imgPath", "")
            .await()

    }

    override suspend fun deleteAccount(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getProvider(): ProviderInfo {
        val providerId = auth.currentUser
            ?.providerData
            ?.firstOrNull { it.providerId != "firebase" }
            ?.providerId

        return ProviderInfo(
            isEmailProvider = providerId == EmailAuthProvider.PROVIDER_ID,
            providerName = if (providerId == EmailAuthProvider.PROVIDER_ID) null
            else providerId?.replace(".com", "")?.replaceFirstChar { it.uppercase() }
        )
    }
}