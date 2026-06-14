package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.tasks.Task
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun scheduleTaskNotification(task: Task, petName: String)
    fun cancelTaskNotification(taskId: String)
    fun isNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}