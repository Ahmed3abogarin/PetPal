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

        val requestCode = task.id.hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            task.dateTime,
            pendingIntent
        )
    }

    override fun cancelTaskNotification(taskId: String) {
        val intent = Intent(context, TaskAlarmReceiver::class.java)

        // Convert the String UUID into a unique, stable Int request code
        val requestCode = taskId.hashCode()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode, // Fixed: Passing the Int instead of the String
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel() // Clean up the PendingIntent from the system OS memory too
    }
}