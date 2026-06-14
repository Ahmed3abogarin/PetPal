package com.vtol.petpal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vtol.petpal.data.repository.AppPrefs.CLOUD_SYNC_KEY
import com.vtol.petpal.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    override fun isCloudSyncEnabled(): Flow<Boolean> =
        dataStore.data.map { it[CLOUD_SYNC_KEY] ?: false }

    override suspend fun setCloudSyncEnabled(enabled: Boolean) {
        dataStore.edit { it[CLOUD_SYNC_KEY] = enabled }
    }
}