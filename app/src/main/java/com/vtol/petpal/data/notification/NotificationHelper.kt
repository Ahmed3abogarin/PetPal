package com.vtol.petpal.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vtol.petpal.R

// data/notification/NotificationHelper.kt
object NotificationHelper {
    private const val CHANNEL_ID = "pet_tasks_channel"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, "Pet Tasks", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Reminders for pet tasks" }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun showNotification(context: Context, taskId: Long, title: String, note: String?) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.pet_placeholder)
            .setContentTitle(title)
            .setContentText(note ?: "Time for your pet task!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(taskId.toInt(), notification)
    }
}