package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.repository.AppRepository

class UpdateTask(
    private val repository: AppRepository
) {
    suspend operator fun invoke(task: TaskUi) {
        repository.updateTask(task)
    }
}