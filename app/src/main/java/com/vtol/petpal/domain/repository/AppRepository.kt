package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun addPet(pet: Pet): Resource<Unit>

    suspend fun getPets(): Resource<List<Pet>>

    suspend fun getPet(id: String): Resource<Pet?>

    suspend fun insertTask(task: Task)

    fun getAllTasks(): Flow<List<Task>>
}