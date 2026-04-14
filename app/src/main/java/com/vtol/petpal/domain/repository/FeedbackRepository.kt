package com.vtol.petpal.domain.repository

interface FeedbackRepository {
    suspend fun submitFeedback(feedback: HashMap<String, Any>): Result<Unit>
}