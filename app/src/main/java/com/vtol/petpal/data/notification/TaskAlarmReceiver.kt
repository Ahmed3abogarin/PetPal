package com.vtol.petpal.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar

class TaskAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val id = intent.getLongExtra("task_id", 0L)
        val title = intent.getStringExtra("task_title") ?: "Task Reminder"
        val repeat = intent.getStringExtra("repeat_interval") ?: "Never"
        val oldTime = intent.getLongExtra("trigger_time", 0L)

        // Show the notification
        NotificationHelper.showNotification(context, taskId = id, title = title)

        // Calculate next trigger time
        val nextTime = when (repeat) {
            "Daily" -> oldTime + AlarmManager.INTERVAL_DAY
            "Weekly" -> oldTime + AlarmManager.INTERVAL_DAY * 7
            "Monthly" -> Calendar.getInstance().apply {
                timeInMillis = oldTime
                add(Calendar.MONTH, 1)
            }.timeInMillis
            else -> null
        }

        // Reschedule the alarm if repeat is set
        if (nextTime != null) {
            val newIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
                putExtra("task_id", id)
                putExtra("task_title", title)
                putExtra("repeat_interval", repeat)
                putExtra("trigger_time", nextTime) // ✅ updated time for next cycle
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context, id.toInt(), newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(AlarmManager::class.java)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextTime,
                pendingIntent
            )
        }
    }
}