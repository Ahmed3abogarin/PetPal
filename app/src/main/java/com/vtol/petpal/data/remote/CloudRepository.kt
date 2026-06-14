package com.vtol.petpal.data.remote

import com.vtol.petpal.domain.model.tasks.Task

interface CloudRepository {
    suspend fun uploadTask(userId: String, task: Task): Result<Unit>
}