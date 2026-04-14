package com.vtol.petpal.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.usecases.feedback.SubmitFeedBackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val submitFeedBackUseCase: SubmitFeedBackUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<FeedbackUiState>(FeedbackUiState.FeedbackForm)
    val state = _state.asStateFlow()


    fun submitFeedback(feedback: HashMap<String, Any>) {

        viewModelScope.launch {
            _state.value = FeedbackUiState.Loading

            submitFeedBackUseCase(feedback)
                .onSuccess { _state.value = FeedbackUiState.Success }
                .onFailure { _state.value = FeedbackUiState.Error }


        }
    }
}