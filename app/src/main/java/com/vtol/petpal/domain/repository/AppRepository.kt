package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun addPet(pet: Pet,image: ByteArray?,weight: WeightRecord): Resource<Unit>

    suspend fun updatePet(pet: Pet, image: ByteArray?): Result<Unit>

    fun getPets(): Flow<List<Pet>>

    fun getPet(id: String): Flow<Pet>

    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: TaskUi)
    suspend fun deleteTask(taskId: String)

    fun getAllTasks(): Flow<List<TaskUi>>

    fun getPetTasks(petId: String): Flow<List<TaskUi>>

    suspend fun getPendingSyncTasks(): List<Task>
    suspend fun updateSyncStatus(taskId: String, status: SyncStatus)
    suspend fun upsertRemoteTasks(tasks: List<Task>)
    suspend fun getTaskById(taskId: String): Task?

    suspend fun addWeight(petId: String,weightRecord: WeightRecord)

    fun getWeightList(petId: String): Flow<List<WeightRecord>>


    suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean)

    fun getSpecificTasks(type: TaskType): Flow<List<Task>>

}