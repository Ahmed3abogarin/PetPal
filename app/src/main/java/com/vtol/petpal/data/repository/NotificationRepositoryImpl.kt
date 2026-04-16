package com.vtol.petpal.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.vtol.petpal.data.notification.TaskAlarmReceiver
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.NotificationRepository

// data/notification/NotificationRepositoryImpl.kt
class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleTaskNotification(task: Task) {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra("task_id", task.id)
            putExtra("task_title", task.title)
            putExtra("repeat_interval", task.repeatInterval?.name)
            putExtra("task_type", task.type.name)
            putExtra("trigger_time", task.dateTime) // ✅ now included
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, task.id.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            task.dateTime,
            pendingIntent
        )
    }

    override fun cancelTaskNotification(taskId: Long) {
        val intent = Intent(context, TaskAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, taskId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}