package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun addPet(pet: Pet,weight: WeightRecord): Resource<Unit>

    fun getPets(): Flow<List<Pet>>

    suspend fun getPet(id: String): Pet

    suspend fun insertTask(task: Task)

    fun getAllTasks(): Flow<List<Task>>

    fun getTask(petId: String): Flow<List<Task>>

    suspend fun addWeight(petId: String,weightRecord: WeightRecord)

    fun getWeightList(petId: String): Flow<List<WeightRecord>>

}