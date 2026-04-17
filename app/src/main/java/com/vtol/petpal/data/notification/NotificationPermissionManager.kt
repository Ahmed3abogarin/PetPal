package com.vtol.petpal.data.notification

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

// data/notification/NotificationPermissionManager.kt
class NotificationPermissionManager(
    private val context: Context
) {
    // Check POST_NOTIFICATIONS (Android 13+)
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // below Android 13 always granted
        }
    }

    // Check SCHEDULE_EXACT_ALARM (Android 12+)
    fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(AlarmManager::class.java)
                .canScheduleExactAlarms()
        } else {
            true // below Android 12 always granted
        }
    }
}