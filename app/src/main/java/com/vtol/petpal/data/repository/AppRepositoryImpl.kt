package com.vtol.petpal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.data.mapper.toTaskModel
import com.vtol.petpal.data.mapper.toUiModel
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Constants.PETS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import com.vtol.petpal.util.Constants.WEIGHT_COLLECTION
import com.vtol.petpal.util.AppStoragePaths.petProfileStoragePath
import com.vtol.petpal.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class AppRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val tasksDao: TasksDao,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val gson: Gson
) : AppRepository {


    override suspend fun addPet(
        pet: Pet,
        image: ByteArray?,
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
            if (image != null) {
                val storageRef = storage.reference.child(
                    petProfileStoragePath(uid, petRef.id)
                )

                storageRef.putBytes(image).await()
                imageUrl = storageRef.downloadUrl.await().toString()
            }

            val newPet = pet.copy(
                id = petRef.id,
                imagePath = imageUrl ?: ""
            )

            // Save pet
            petRef.set(newPet).await()
            val dayKey = LocalDate.now().toString()
            // Save initial weight
            petRef.collection(WEIGHT_COLLECTION)
                .document(dayKey)
                .set(weight)
                .await()

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun updatePet(
        pet: Pet,
        image: ByteArray?
    ): Result<Unit> = runCatching {

        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")

        val petRef = firestore
            .collection(USERS_COLLECTION)
            .document(currentUid)
            .collection(PETS_COLLECTION)
            .document(pet.id)

        var imageUrl = pet.imagePath

        if (image != null) {
            val storageRef = storage.reference.child(
                petProfileStoragePath(currentUid, pet.id)
            )

            storageRef.putBytes(image).await()
            imageUrl = storageRef.downloadUrl.await().toString()
        }

        val updatedPet = pet.copy(
            imagePath = imageUrl,
            updatedAt = System.currentTimeMillis()
        )

        petRef.set(updatedPet).await()
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

    override fun getPet(id: String): Flow<Pet> = callbackFlow {

        val uid = auth.currentUser?.uid

        if (uid == null) {
            close(Exception("User not authenticated"))
            return@callbackFlow
        }

        val listener = firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .collection(PETS_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pet = snapshot
                    ?.toObject(Pet::class.java)

                if (pet != null) {
                    trySend(pet)
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun insertTask(task: Task): Long =
        tasksDao.insertTask(task)

    override suspend fun updateTask(task: TaskUi) {
        val newTask = task.toTaskModel(gson)
        tasksDao.updateTask(newTask)
    }

    override suspend fun deleteTask(taskId: String) {
        return tasksDao.deleteTask(taskId)
    }

    override fun getAllTasks(): Flow<List<TaskUi>> =
        tasksDao.getAllTasks().map { entities ->
            entities.map { entity -> entity.toUiModel(gson) }
        }

    override fun getPetTasks(petId: String): Flow<List<TaskUi>> =
        tasksDao.getPetTasks(petId).map { entities ->
            entities.map { entity -> entity.toUiModel(gson) }
        }

    override suspend fun getPendingSyncTasks(): List<Task> =
        tasksDao.getPendingSyncTasks() // no mapping needed

    override suspend fun updateSyncStatus(taskId: String, status: SyncStatus) =
        tasksDao.updateSyncStatus(taskId, status.name)

    override suspend fun upsertRemoteTasks(tasks: List<Task>) {
        tasksDao.upsertRemoteTasks(tasks)
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return tasksDao.getTaskById(taskId)
    }

    override suspend fun addWeight(petId: String, weightRecord: WeightRecord) {
        val uid = auth.currentUser?.uid
        uid?.let {
            val dayKey = LocalDate.now().toString()

            firestore.collection(USERS_COLLECTION)
                .document(it)
                .collection(PETS_COLLECTION)
                .document(petId)
                .collection(WEIGHT_COLLECTION)
                .document(dayKey)
                .set(weightRecord)
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

    override suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        tasksDao.updateTaskCompletion(taskId, isCompleted)
    }

    override fun getSpecificTasks(type: TaskType): Flow<List<Task>> {
        return tasksDao.getSpecificTasks(type)
    }


}