package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.NotificationRepository

class DeleteTask(
    private val appRepository: AppRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(taskId: String) {
        appRepository.deleteTask(taskId)
        notificationRepository.cancelTaskNotification(taskId)
    }
}