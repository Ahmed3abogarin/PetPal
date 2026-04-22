package com.vtol.petpal.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.feedback.SubmitFeedBackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val submitFeedBackUseCase: SubmitFeedBackUseCase,
    private val appUseCases: AppUseCases
) : ViewModel() {


    private val _state = MutableStateFlow<FeedbackUiState>(FeedbackUiState.FeedbackForm)
    val state = _state.asStateFlow()


    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getVetVisits()
    }
    fun submitFeedback(feedback: HashMap<String, Any>) {

        viewModelScope.launch {
            _state.value = FeedbackUiState.Loading

            submitFeedBackUseCase(feedback)
                .onSuccess { _state.value = FeedbackUiState.Success }
                .onFailure { _state.value = FeedbackUiState.Error }
        }
    }

    fun getVetVisits() {
        _uiState.update { it.copy(loading = true) }

        appUseCases.getSpecificTasks(TaskType.VET)
            .onEach { tasks ->
            _uiState.update { it.copy(vetVisits = tasks.size) }
        }.launchIn(viewModelScope)


    }

}

data class ProfileUiState(
    val loading: Boolean = true,
    val vetVisits: Int = 0,
)