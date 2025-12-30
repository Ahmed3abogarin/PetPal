package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.usecases.tasks.GetTasksById
import com.vtol.petpal.domain.usecases.tasks.GetTasks
import com.vtol.petpal.domain.usecases.tasks.InsertTask

data class AppUseCases(
    val addPet: AddPet,
    val getPets: GetPets,
    val getPet: GetPet,
    val insertTask: InsertTask,
    val getTasks: GetTasks,
    val getTasksById: GetTasksById,
    val addWeight: AddWeight,
    val getWeights: GetWeights
)