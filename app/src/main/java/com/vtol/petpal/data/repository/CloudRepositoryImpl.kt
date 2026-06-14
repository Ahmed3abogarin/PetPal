package com.vtol.petpal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.remote.CloudRepository
import com.vtol.petpal.data.remote.TaskRemoteDto
import com.vtol.petpal.data.remote.toDomain
import com.vtol.petpal.data.remote.toRemoteDto
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.util.Constants.TASKS_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CloudRepository {

    override suspend fun uploadTask(userId: String, task: Task): Result<Unit> = runCatching {
        firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(TASKS_COLLECTION)
            .document(task.id)
            .set(task.toRemoteDto())
            .await()
    }

    override suspend fun fetchAllTasks(): Result<List<Task>> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")

        firestore
            .collection(USERS_COLLECTION)
            .document(currentUid)
            .collection(TASKS_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(TaskRemoteDto::class.java)?.toDomain()
            }
    }
}