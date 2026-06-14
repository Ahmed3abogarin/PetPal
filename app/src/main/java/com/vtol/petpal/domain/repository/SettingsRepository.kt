package com.vtol.petpal.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun isCloudSyncEnabled(): Flow<Boolean>

    suspend fun setCloudSyncEnabled(enabled: Boolean)
}