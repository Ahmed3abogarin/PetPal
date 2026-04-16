package com.vtol.petpal.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TaskAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId    = intent.getLongExtra("task_id", -1)
        val taskTitle = intent.getStringExtra("task_title") ?: return
        val taskNote  = intent.getStringExtra("task_note")
        NotificationHelper.showNotification(context, taskId, taskTitle, taskNote)
    }
}