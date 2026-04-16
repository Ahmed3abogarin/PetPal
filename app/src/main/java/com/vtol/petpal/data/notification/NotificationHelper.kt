package com.vtol.petpal.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vtol.petpal.R

object NotificationHelper {
    private const val CHANNEL_ID = "pet_tasks_channel"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, "Pet Tasks", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Reminders for pet tasks" }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun showNotification(context: Context, taskId: Long, title: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.pet_placeholder)
            .setContentTitle(title)
            .setContentText("Time for your pet task!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(taskId.toInt(), notification)
        }
    }
}