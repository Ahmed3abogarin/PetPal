package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.data.remote.CloudRepository
import com.vtol.petpal.domain.repository.AppRepository
import javax.inject.Inject

class RestoreTasksUseCase @Inject constructor(
    private val cloudRepository: CloudRepository,
    private val repository: AppRepository
) {
    suspend operator fun invoke(): Result<Int> = runCatching {
        cloudRepository.fetchAllTasks()
            .onSuccess { remoteTasks ->
                repository.upsertRemoteTasks(remoteTasks)
            }
            .getOrThrow()
            .size
    }
}