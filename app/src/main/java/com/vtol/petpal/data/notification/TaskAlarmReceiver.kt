package com.vtol.petpal.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.toTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint // Allows Hilt to inject dependencies into this receiver
class TaskAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appUseCases: AppUseCases

    // TaskAlarmReceiver.kt — fix both toInt() calls and valueOf crash

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        val id = intent.getStringExtra("task_id") ?: ""
        val name = intent.getStringExtra("pet_name") ?: "your pet"
        val repeat = intent.getStringExtra("repeat_interval") ?: "Never"
        val type = intent.getStringExtra("task_type") ?: "Unknown"
        val oldTime = intent.getLongExtra("trigger_time", 0L)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val task = appUseCases.getTaskById(id)

                val triggerDate = Instant.ofEpochMilli(oldTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                if (task != null && !task.deletedDates.contains(triggerDate)) {
                    // Safe enum parsing — won't crash on unknown values
                    val taskType = runCatching { TaskType.valueOf(type) }.getOrNull()

                    if (taskType != null) {
                        val (title, message) = when (taskType) {
                            TaskType.FEED -> Pair("🍖 Feeding Reminder", "It's time to feed $name. Scheduled for ${oldTime.toTimeString()}.")
                            TaskType.WALK -> Pair("🐕 Walk Reminder", "Walk $name is due now.")
                            TaskType.MEDICATION -> Pair("💊 Medication Reminder", "It's time to give $name their medication.")
                            TaskType.VET -> Pair("🩺 Vet Reminder", "$name has a scheduled vet task.")
                        }

                        NotificationHelper.showNotification(context, taskId = id, title = title, message = message)
                    }
                }

                val nextTime = when (repeat) {
                    "Daily" -> oldTime + AlarmManager.INTERVAL_DAY
                    "Weekly" -> oldTime + AlarmManager.INTERVAL_DAY * 7
                    "Monthly" -> Calendar.getInstance().apply {
                        timeInMillis = oldTime
                        add(Calendar.MONTH, 1)
                    }.timeInMillis
                    else -> null
                }

                if (nextTime != null) {
                    val newIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
                        putExtra("task_id", id)
                        putExtra("pet_name", name)
                        putExtra("task_type", type)
                        putExtra("repeat_interval", repeat)
                        putExtra("trigger_time", nextTime)
                    }

                    // Use hashCode() instead of toInt() — safe for UUIDs
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, id.hashCode(), newIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val alarmManager = context.getSystemService(AlarmManager::class.java)
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextTime,
                        pendingIntent
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}