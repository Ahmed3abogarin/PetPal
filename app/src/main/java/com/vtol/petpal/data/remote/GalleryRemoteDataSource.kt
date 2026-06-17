package com.vtol.petpal.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.vtol.petpal.domain.model.PetPhoto
import com.vtol.petpal.util.AppStoragePaths
import com.vtol.petpal.util.Constants.GALLERY_COLLECTION
import com.vtol.petpal.util.Constants.PETS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class GalleryRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    fun getPhotos(petId: String): Flow<List<PetPhoto>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: run {
            close(Exception("Not authenticated"))
            return@callbackFlow
        }

        val listener = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(PETS_COLLECTION)
            .document(petId)
            .collection(GALLERY_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val photos = snapshot?.documents
                    ?.mapNotNull { it.toObject(PetPhoto::class.java) }
                    ?: emptyList()
                trySend(photos)
            }
        awaitClose { listener.remove() }
    }

    suspend fun uploadPhoto(petId: String, image: ByteArray): Result<Unit> = runCatching {
        val userId = auth.currentUser?.uid ?: throw Exception("Not authenticated")
        val photoId = UUID.randomUUID().toString()

        // 1. upload to Storage
        val ref = storage.reference
            .child(AppStoragePaths.petGalleryStoragePath(userId, petId, photoId))

        ref.putBytes(image).await()
        val downloadUrl = ref.downloadUrl.await().toString()

        // 2. build photo only when we have everything
        val photo = PetPhoto(
            id = photoId,
            petId = petId,
            url = downloadUrl
        )

        // 3. save to Firestore
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(PETS_COLLECTION)
            .document(petId)
            .collection(GALLERY_COLLECTION)
            .document(photoId)
            .set(photo)
            .await()
    }

    suspend fun deletePhoto(photo: PetPhoto): Result<Unit> = runCatching {
        val userId = auth.currentUser?.uid ?: throw Exception("Not authenticated")

        // 1- Delete photo from storage
        storage.reference
            .child(AppStoragePaths.petGalleryStoragePath(userId, photo.petId, photo.id))
            .delete()
            .await()

        // 2- Delete the url
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(PETS_COLLECTION)
            .document(photo.petId)
            .collection(GALLERY_COLLECTION)
            .document(photo.id)
            .delete()
            .await()
    }
}