package com.vtol.petpal.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.util.toTimeString
import java.util.Calendar

class TaskAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val id = intent.getLongExtra("task_id", 0L)
        val name = intent.getStringExtra("pet_name") ?: "your pet"
        val repeat = intent.getStringExtra("repeat_interval") ?: "Never"
        val type = intent.getStringExtra("task_type") ?: "Unknown"
        val oldTime = intent.getLongExtra("trigger_time", 0L)

        val taskType = TaskType.valueOf(type)

        val (title, message) = when(taskType) {
            TaskType.FEED -> Pair("\uD83C\uDF56 Feeding Reminder","It's time to feed $name. Scheduled for ${oldTime.toTimeString()}.")
            TaskType.WALK -> Pair("\uD83D\uDC15 Walk Reminder","Walk $name is due now.")
            TaskType.MEDICATION -> Pair("\uD83D\uDC8A Medication Reminder", "It's time to give $name their medication.")
            TaskType.VET -> Pair("\uD83E\uDE7A Vet Reminder", "$name has a scheduled vet task.")
        }

        // Show the notification
        NotificationHelper.showNotification(context, taskId = id, title = title, message = message)

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
                putExtra("trigger_time", nextTime)
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