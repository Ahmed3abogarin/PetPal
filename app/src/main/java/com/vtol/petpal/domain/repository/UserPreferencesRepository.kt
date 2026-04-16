package com.vtol.petpal.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val notificationsEnabled: Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}