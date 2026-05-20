package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow


class GetTasks(
    private val appRepository: AppRepository
) {
    operator fun invoke(): Flow<List<TaskUi>>{
        return appRepository.getAllTasks()
    }
}