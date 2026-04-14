package com.vtol.petpal.domain.usecases.feedback

import com.vtol.petpal.domain.repository.FeedbackRepository

class SubmitFeedBackUseCase(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(feedback: HashMap<String, Any>): Result<Unit> {
        return feedbackRepository.submitFeedback(feedback)
    }
}