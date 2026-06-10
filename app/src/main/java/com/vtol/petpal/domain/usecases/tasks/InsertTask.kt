package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.first

class InsertTask(
    private val appRepository: AppRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(task: Task, petName: String){
        val insertedId = appRepository.insertTask(task)

        val notificationsEnabled = notificationRepository.isNotificationsEnabled().first()

        if (notificationsEnabled) {
            notificationRepository.scheduleTaskNotification(
                task.copy(id = insertedId),
                petName
            )
        }
    }
}