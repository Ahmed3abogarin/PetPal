package com.vtol.petpal.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Constants.PETS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import com.vtol.petpal.util.Constants.WEIGHT_COLLECTION
import com.vtol.petpal.util.PetStoragePaths.petProfileStoragePath
import com.vtol.petpal.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AppRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val tasksDao: TasksDao,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : AppRepository {


    override suspend fun addPet(
        pet: Pet,
        imageUri: Uri?,
        weight: WeightRecord
    ): Resource<Unit> {

        val uid = auth.currentUser?.uid
            ?: return Resource.Error("User not authenticated")

        return try {
            val petRef = firestore
                .collection(USERS_COLLECTION)
                .document(uid)
                .collection(PETS_COLLECTION)
                .document()

            var imageUrl: String? = null

            // Upload image ONLY if exists
            if (imageUri != null) {
                val storageRef = storage.reference.child(
                    petProfileStoragePath(uid, petRef.id)
                )

                storageRef.putFile(imageUri).await()
                imageUrl = storageRef.downloadUrl.await().toString()
            }

            val newPet = pet.copy(
                id = petRef.id,
                imagePath = imageUrl ?: ""
            )

            // Save pet
            petRef.set(newPet).await()

            // Save initial weight
            petRef.collection(WEIGHT_COLLECTION)
                .add(weight)
                .await()

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getPets(): Flow<List<Pet>> = callbackFlow {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = firestore.collection(USERS_COLLECTION)
            .document(uid)
            .collection(PETS_COLLECTION)

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val pets = snapshot?.toObjects(Pet::class.java) ?: emptyList()
            trySend(pets)
        }

        awaitClose {
            listener.remove()
        }
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


    override suspend fun getPet(id: String): Pet {
        val uid = auth.currentUser?.uid
        uid?.let {
            val pet = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .collection(PETS_COLLECTION)
                .document(id)
                .get().await().toObject(Pet::class.java)


            return pet ?: throw Exception("Pet not found")
        }
        return Pet()

    }

    override suspend fun insertTask(task: Task): Long =
        tasksDao.insertTask(task)

    override fun getAllTasks(): Flow<List<Task>> =
        tasksDao.getAllTasks()

    override fun getTask(petId: String): Flow<List<Task>> =
        tasksDao.getTask(petId)

    override suspend fun addWeight(petId: String, weightRecord: WeightRecord) {
        val uid = auth.currentUser?.uid
        uid?.let {
            firestore.collection(USERS_COLLECTION)
                .document(it)
                .collection(PETS_COLLECTION)
                .document(petId)
                .collection(WEIGHT_COLLECTION)
                .add(weightRecord)
                .await()
        }
    }

    override fun getWeightList(petId: String): Flow<List<WeightRecord>> = callbackFlow {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .collection(PETS_COLLECTION)
            .document(petId)
            .collection(WEIGHT_COLLECTION)
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val weights =
                    snapshot?.toObjects(WeightRecord::class.java) ?: emptyList()

                trySend(weights)
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) {
        tasksDao.updateTaskCompletion(taskId, isCompleted)
    }

    override fun getSpecificTasks(type: TaskType): Flow<List<Task>> {
        return tasksDao.getSpecificTasks(type)
    }


}