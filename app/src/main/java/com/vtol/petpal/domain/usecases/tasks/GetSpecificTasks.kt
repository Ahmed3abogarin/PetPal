package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetSpecificTasks(
    private val appRepository: AppRepository
) {
    operator fun invoke(type: TaskType): Flow<List<Task>> =
        appRepository.getSpecificTasks(type)
}