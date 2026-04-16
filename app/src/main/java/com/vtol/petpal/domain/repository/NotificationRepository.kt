package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.tasks.Task

interface NotificationRepository {
    fun scheduleTaskNotification(task: Task)
    fun cancelTaskNotification(taskId: Long)
}