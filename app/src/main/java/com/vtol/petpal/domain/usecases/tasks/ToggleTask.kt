package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.repository.AppRepository

class ToggleTask(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(taskId: Int, isCompleted: Boolean) {
        appRepository.toggleTaskCompletion(taskId, isCompleted)
    }
}