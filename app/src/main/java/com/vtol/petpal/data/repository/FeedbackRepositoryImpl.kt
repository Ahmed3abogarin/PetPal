package com.vtol.petpal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.domain.repository.FeedbackRepository
import com.vtol.petpal.util.Constants.FEEDBACK_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FeedbackRepository {

    override suspend fun submitFeedback(feedback: HashMap<String, Any>): Result<Unit> {
        return runCatching {
            firestore.collection(FEEDBACK_COLLECTION)
                .add(feedback)
                .await()
        }
    }

}