package com.vtol.petpal.domain.repository

import android.net.Uri
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun addPet(pet: Pet,imageUri: Uri?,weight: WeightRecord): Resource<Unit>

    fun getPets(): Flow<List<Pet>>

    suspend fun getPet(id: String): Pet

    suspend fun insertTask(task: Task): Long

    fun getAllTasks(): Flow<List<Task>>

    fun getTask(petId: String): Flow<List<Task>>

    suspend fun addWeight(petId: String,weightRecord: WeightRecord)

    fun getWeightList(petId: String): Flow<List<WeightRecord>>


    suspend fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean)

    fun getSpecificTasks(type: TaskType): Flow<List<Task>>

}