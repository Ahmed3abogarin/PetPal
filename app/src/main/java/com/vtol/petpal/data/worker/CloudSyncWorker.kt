package com.vtol.petpal.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.vtol.petpal.data.remote.CloudRepository
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.repository.AppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

// data/worker/CloudSyncWorker.kt
@HiltWorker
class CloudSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: AppRepository,
    private val cloudRepository: CloudRepository,
    private val auth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val userId = auth.currentUser?.uid ?: return Result.failure()


        val pendingTasks = taskRepository.getPendingSyncTasks()

        Timber.tag("CloudSync").d(pendingTasks.size.toString())
        if (pendingTasks.isEmpty()) return Result.success()


        var hasFailed = false

        pendingTasks.forEach { task ->

            cloudRepository.uploadTask(userId, task)
                .onSuccess {
                    Timber.d("Task ${task.id} uploaded successfully")

                    taskRepository.updateSyncStatus(task.id, SyncStatus.SYNCED)
                }
                .onFailure {
                    Timber.tag("CloudSync").d(it.message ?: "Unknown error")
                    taskRepository.updateSyncStatus(task.id, SyncStatus.PENDING)
                    hasFailed = true
                }
        }

        // retry later if any failed
        return if (hasFailed) Result.retry() else Result.success()
    }

    companion object {
        const val WORK_NAME = "cloud_sync_work"
    }
}