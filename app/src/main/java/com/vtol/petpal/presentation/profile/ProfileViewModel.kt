package com.vtol.petpal.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.user.UpdateUserImageUseCase
import com.vtol.petpal.domain.usecases.feedback.SubmitFeedBackUseCase
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val submitFeedBackUseCase: SubmitFeedBackUseCase,
    private val appUseCases: AppUseCases,
    private val authUseCases: AuthUseCases,
    private val updateUserImageUseCase: UpdateUserImageUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<FeedbackUiState>(FeedbackUiState.FeedbackForm)
    val state = _state.asStateFlow()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getUser()
        getVetVisits()
    }


    fun onEvent(event: ProfileEvents) {
        when (event) {
            is ProfileEvents.SignOut -> signOut()
            is ProfileEvents.UpdateImage -> updateUserImage(event.uri)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authUseCases.logout()
        }
    }


    private fun getUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUserLoading = true) }
            appUseCases.getUser()
                .catch { e -> _uiState.update { it.copy(isUserLoading = false, error = e.message) } }
                .collect { user -> _uiState.update { it.copy(isUserLoading = false, user = user) } }
        }
    }

    private fun getVetVisits() {
        _uiState.update { it.copy(isVetVisitsLoading = true) }
        appUseCases.getSpecificTasks(TaskType.VET)
            .onEach { tasks ->
                _uiState.update { it.copy(vetVisits = tasks.size, isVetVisitsLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun updateUserImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImageUploading = true) }


            val result = updateUserImageUseCase(uri)

            result
                .onSuccess { imagePath ->
                    Timber.d(">>> onSuccess called")
                    _uiState.update {
                        it.copy(
                            isImageUploading = false,
                            user = it.user?.copy(imgPath = imagePath)
                        )
                    }
                }
                .onFailure { e ->
                    Timber.e(">>> onFailure called: ${e.message}")
                    _uiState.update { it.copy(isImageUploading = false, error = e.message) }
                }
        }
    }


    fun submitFeedback(feedback: HashMap<String, Any>) {

        viewModelScope.launch {
            _state.value = FeedbackUiState.Loading

            submitFeedBackUseCase(feedback)
                .onSuccess { _state.value = FeedbackUiState.Success }
                .onFailure {
                    Timber.tag("feedback").e(it)
                    _state.value =
                        FeedbackUiState.Error
                }
        }
    }
}

data class ProfileUiState(
    val isUserLoading: Boolean = false,
    val isVetVisitsLoading: Boolean = false,
    val isImageUploading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val vetVisits: Int = 0,
)