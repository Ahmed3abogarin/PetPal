package com.vtol.petpal.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppPrefs {
    val COMPLETED = booleanPreferencesKey("onboarding_completed")
    val NOTIFICATION_ENABLED = booleanPreferencesKey("notifications_enabled")
    val CLOUD_SYNC_KEY = booleanPreferencesKey("cloud_sync_enabled")
}