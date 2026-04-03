package com.vtol.petpal.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.vtol.petpal.domain.model.AppVersion
import com.vtol.petpal.domain.repository.UpdateRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
): UpdateRepository {
    override suspend fun getVersion(): AppVersion {

        return try {
            remoteConfig.fetchAndActivate().await()
            val minVersion = remoteConfig.getLong("min_required_version").toInt()
            val latestVersion = remoteConfig.getLong("latest_version").toInt()
            AppVersion(minVersion = minVersion, latestVersion = latestVersion)
        }catch (_: Exception){
            AppVersion(1,1)
        }
    }

}