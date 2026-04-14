package com.vtol.petpal.presentation.profile

sealed class FeedbackUiState {
    data object FeedbackForm: FeedbackUiState()
    data object Loading: FeedbackUiState()
    data object Success: FeedbackUiState()
    data object Error: FeedbackUiState()
}