package com.vtol.petpal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {
    override suspend fun getUser(): Resource<User> {

        return try {
            val currentUid = auth.currentUser?.uid ?: return Resource.Error("User Not found")
            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(currentUid)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java) ?: return Resource.Error("User not found")

            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }

    }
}