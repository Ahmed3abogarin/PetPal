package com.vtol.petpal.data.worker

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// data/worker/SyncScheduler.kt
class SyncScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<CloudSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            CloudSyncWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,  // don't cancel if already running
            request
        )
    }

    fun cancelSync() {
        workManager.cancelUniqueWork(CloudSyncWorker.WORK_NAME)
    }
}