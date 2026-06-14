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

    override fun onReceive(context: Context, intent: Intent) {
        // goAsync() keeps the Receiver alive while Coroutines do background database work
        val pendingResult = goAsync()

        val id = intent.getStringExtra("task_id") ?: ""
        val name = intent.getStringExtra("pet_name") ?: "your pet"
        val repeat = intent.getStringExtra("repeat_interval") ?: "Never"
        val type = intent.getStringExtra("task_type") ?: "Unknown"
        val oldTime = intent.getLongExtra("trigger_time", 0L)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Fetch the task from the database
                // Note: Make sure getTaskById exists in your AppUseCases and returns a single TaskUi or Entity!
                val task = appUseCases.getTaskById(id)

                // 2. Convert trigger time to LocalDate to check exclusions
                val triggerDate = Instant.ofEpochMilli(oldTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                // 3. Show notification ONLY if task exists and this date isn't deleted
                if (task != null && !task.deletedDates.contains(triggerDate)) {
                    val taskType = TaskType.valueOf(type)

                    val (title, message) = when(taskType) {
                        TaskType.FEED -> Pair("\uD83C\uDF56 Feeding Reminder","It's time to feed $name. Scheduled for ${oldTime.toTimeString()}.")
                        TaskType.WALK -> Pair("\uD83D\uDC15 Walk Reminder","Walk $name is due now.")
                        TaskType.MEDICATION -> Pair("\uD83D\uDC8A Medication Reminder", "It's time to give $name their medication.")
                        TaskType.VET -> Pair("\uD83E\uDE7A Vet Reminder", "$name has a scheduled vet task.")
                    }

                    // Show the notification
                    NotificationHelper.showNotification(context, taskId = id, title = title, message = message)
                }

                // 4. Always calculate and schedule next trigger time so the alarm chain doesn't break
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
                        putExtra("pet_name", name)
                        putExtra("task_type", type)
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
            } finally {
                // Crucial: Tell Android the background work is done, preventing memory leaks/crashes
                pendingResult.finish()
            }
        }
    }
}