package com.vtol.petpal.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vtol.petpal.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    companion object {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    override val notificationsEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }
}