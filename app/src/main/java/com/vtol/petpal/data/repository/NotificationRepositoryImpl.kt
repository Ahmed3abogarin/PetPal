package com.vtol.petpal.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vtol.petpal.data.notification.TaskAlarmReceiver
import com.vtol.petpal.data.repository.AppPrefs.NOTIFICATION_ENABLED
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepositoryImpl(
    private val context: Context,
    private val dataStore: DataStore<Preferences>,
): NotificationRepository {

    override fun isNotificationsEnabled(): Flow<Boolean> {
        return dataStore.data.map { it[NOTIFICATION_ENABLED] ?: true }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATION_ENABLED] = enabled }
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleTaskNotification(task: Task, petName: String) {
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra("task_id", task.id)
            putExtra("repeat_interval", task.repeatInterval?.name)
            putExtra("task_type", task.type.name)
            putExtra("trigger_time", task.dateTime)
            putExtra("pet_name", petName)
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