package com.vtol.petpal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.vtol.petpal.data.repository.AppPrefs.PREMIUM_KEY
import com.vtol.petpal.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PremiumRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PremiumRepository {

    override fun isPremium(): Flow<Boolean> =
        dataStore.data.map { it[PREMIUM_KEY] ?: false }

    suspend fun setPremium(isPremium: Boolean) {
        dataStore.edit { it[PREMIUM_KEY] = isPremium }
    }
}