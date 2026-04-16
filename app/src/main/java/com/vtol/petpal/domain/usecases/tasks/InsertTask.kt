package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.repository.NotificationRepository
import com.vtol.petpal.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class InsertTask(
    private val appRepository: AppRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(task: Task){
        val insertedId = appRepository.insertTask(task)

        val notificationsEnabled = preferencesRepository
            .notificationsEnabled.first()

        if (notificationsEnabled) {
            notificationRepository.scheduleTaskNotification(
                task.copy(id = insertedId)
            )
        }
    }
}