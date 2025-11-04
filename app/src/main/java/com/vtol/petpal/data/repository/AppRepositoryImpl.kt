package com.vtol.petpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Constants.PETS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import com.vtol.petpal.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AppRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AppRepository {
    override suspend fun addPet(pet: Pet): Resource<Unit> {
        return try {
            val petId =  firestore.collection(USERS_COLLECTION)
                .document("userId")
                .collection(PETS_COLLECTION)
                .document().id
            val newPet = pet.copy(id = petId)
            firestore.collection(USERS_COLLECTION)
                .document("userId")
                .collection(PETS_COLLECTION)
                .document(newPet.id)
                .set(newPet)
                .await()
           Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}