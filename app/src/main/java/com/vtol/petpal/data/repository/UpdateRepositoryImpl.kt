package com.vtol.petpal.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.vtol.petpal.domain.model.AppVersion
import com.vtol.petpal.domain.repository.UpdateRepository
import com.vtol.petpal.util.Constants.LATEST_VERSION
import com.vtol.petpal.util.Constants.MIN_REQUIRED_VERSION
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
): UpdateRepository {
    init {
        val configSettings = remoteConfigSettings {
            // Change this for production (e,g 3600)
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(mapOf(
            MIN_REQUIRED_VERSION to 1,
            LATEST_VERSION to 1
        ))
    }
    override suspend fun getVersion(): AppVersion {
        return try {
            withTimeout(5000){
                remoteConfig.fetchAndActivate().await()
                val minVersion = remoteConfig.getLong(MIN_REQUIRED_VERSION).toInt()
                val latestVersion = remoteConfig.getLong(LATEST_VERSION).toInt()
                AppVersion(minVersion = minVersion, latestVersion = latestVersion)
            }

        } catch (_: Exception){
            AppVersion(1,1)
        }
    }
}
