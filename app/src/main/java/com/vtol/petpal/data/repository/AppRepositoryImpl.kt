package com.vtol.petpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Constants.PETS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import com.vtol.petpal.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AppRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val tasksDao: TasksDao,
) : AppRepository {
    override suspend fun addPet(pet: Pet): Resource<Unit> {
        return try {
            val petId = firestore.collection(USERS_COLLECTION)
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
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getPets(): Flow<List<Pet>> = callbackFlow {
        val ref = firestore.collection(USERS_COLLECTION)
            .document("userId")
            .collection(PETS_COLLECTION)

        val subscription = ref
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pets = snapshot?.toObjects(Pet::class.java) ?: emptyList()
                trySend(pets) // Send the list to the Flow
            }

        // Important: Clean up the listener when the Flow is closed
        awaitClose { subscription.remove() }
    }


//        return try {
//            val pets =firestore.collection(USERS_COLLECTION)
//                .document("userId")
//                .collection(PETS_COLLECTION)
//                .get().await()
//                .toObjects(Pet::class.java)
//            Resource.Success(pets)
//
//        } catch (e: Exception){
//            Resource.Error(e.message ?: "Unknown error")
//        }


    override suspend fun getPet(id: String): Resource<Pet?> {
        return try {
            val pet = firestore.collection(USERS_COLLECTION)
                .document("userId")
                .collection(PETS_COLLECTION)
                .document(id)
                .get().await().toObject(Pet::class.java)

            Resource.Success(pet)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun insertTask(task: Task) {
        tasksDao.insertTask(task)
    }

    override fun getAllTasks(): Flow<List<Task>> =
        tasksDao.getAllTasks()

}