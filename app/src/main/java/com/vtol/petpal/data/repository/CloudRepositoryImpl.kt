package com.vtol.petpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.remote.CloudRepository
import com.vtol.petpal.data.remote.toRemoteDto
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.util.Constants.TASKS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CloudRepository {

    override suspend fun uploadTask(userId: String, task: Task): Result<Unit> = runCatching {
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(TASKS_COLLECTION)
            .document(task.id.toString())
            .set(task.toRemoteDto())
            .await()
    }
}