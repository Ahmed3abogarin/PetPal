package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.AppRepository

class GetTaskById(
    private val repository: AppRepository
) {
    suspend operator fun invoke(taskId: String): Task? =
        repository.getTaskById(taskId)
}