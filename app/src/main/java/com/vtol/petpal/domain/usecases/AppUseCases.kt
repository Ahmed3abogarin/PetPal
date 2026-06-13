package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.usecases.tasks.DeleteTask
import com.vtol.petpal.domain.usecases.tasks.GetSpecificTasks
import com.vtol.petpal.domain.usecases.tasks.GetPetTasks
import com.vtol.petpal.domain.usecases.tasks.GetTaskById
import com.vtol.petpal.domain.usecases.tasks.GetTasks
import com.vtol.petpal.domain.usecases.tasks.InsertTask
import com.vtol.petpal.domain.usecases.tasks.ToggleTask
import com.vtol.petpal.domain.usecases.tasks.UpdateTask

data class AppUseCases(
    val addPet: AddPet,
    val updatePet: UpdatePet,
    val getPets: GetPets,
    val getPet: GetPet,
    val insertTask: InsertTask,
    val updateTask: UpdateTask,
    val getTaskById: GetTaskById,
    val deleteTask: DeleteTask,
    val getTasks: GetTasks,
    val getPetTasks: GetPetTasks,
    val addWeight: AddWeight,
    val getWeights: GetWeights,

    val getUser: GetUser,

    val getVersion: GetVersion,

    val toggleTask: ToggleTask,

    val toggleNotification: ToggleNotification,
    val getNotificationStatus: GetNotificationStatus,

    val getSpecificTasks: GetSpecificTasks

)