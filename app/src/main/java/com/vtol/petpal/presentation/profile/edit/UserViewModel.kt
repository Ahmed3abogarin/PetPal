package com.vtol.petpal.presentation.profile.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.GoogleAuthProvider
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.user.DeleteAccount
import com.vtol.petpal.domain.usecases.user.GetProvider
import com.vtol.petpal.domain.usecases.user.RemoveUserImage
import com.vtol.petpal.domain.usecases.user.UpdatePassword
import com.vtol.petpal.domain.usecases.user.UpdatePhoneNumber
import com.vtol.petpal.domain.usecases.user.UpdateUserImageUseCase
import com.vtol.petpal.domain.usecases.user.UpdateUsername
import com.vtol.petpal.presentation.register.GoogleAuthUiClient
import com.vtol.petpal.util.AnalyticsParams.EDIT_PROFILE_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val updatePasswordUseCase: UpdatePassword,
    private val updatePhoneNumberUseCase: UpdatePhoneNumber,
    private val updateUsernameUseCase: UpdateUsername,
    private val removeUserImage: RemoveUserImage,
    private val getProviderInfo: GetProvider,
    private val deleteAccountUseCase: DeleteAccount,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val analyticsHelper: FirebaseAnalyticsHelper
) : ViewModel() {
    private val _state = MutableStateFlow(UserUiState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            val providerInfo = getProviderInfo()
            _state.update {
                it.copy(
                    isEmailProvider = providerInfo.isEmailProvider,
                    providerName = providerInfo.providerName
                )
            }
        }
        getUser()
    }

    fun onEvent(event: EditEvents) {
        when (event) {
            is EditEvents.RemoveImage -> removeImage()
            is EditEvents.UpdateImage -> updateUserImage(event.uri)
            is EditEvents.UpdatePassword -> updatePassword(event.old, event.new)
            is EditEvents.UpdatePhone -> updatePhoneNumber(event.phone)
            is EditEvents.UpdateUsername -> updateUsername(event.name)
            is EditEvents.ErrorShown -> _state.update { it.copy(message = null) }
            is EditEvents.DeleteAccount -> deleteAccount(event.credential)
            is EditEvents.ReAuthWithGoogle -> reAuthWithGoogle(event.context)

            is EditEvents.LogScreenView -> analyticsHelper.logScreenView(EDIT_PROFILE_SCREEN)
        }
    }

    private fun reAuthWithGoogle(activityContext: Context) {
        viewModelScope.launch {
            viewModelScope.launch {
                val token =
                    googleAuthUiClient.signIn(activityContext) // inject context via @ApplicationContext
                if (token == null) {
                    _state.update { it.copy(message = "Google sign in failed") }
                    return@launch
                }
                val credential = GoogleAuthProvider.getCredential(token, null)
                deleteAccount(ReAuthCredential.Social(credential))
            }
        }
    }

    private fun deleteAccount(credential: ReAuthCredential) {
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteAccountUseCase(credential)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, message = e.message) }
                }
        }
    }

    private fun updatePhoneNumber(phone: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updatePhoneNumberUseCase(phone)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, message = "Phone number updated") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, message = e.message) }
                }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            appUseCases.getUser()
                .catch { e -> _state.update { it.copy(isLoading = false, message = e.message) } }
                .collect { user -> _state.update { it.copy(isLoading = false, user = user) } }
        }
    }

    private fun removeImage() {
        viewModelScope.launch {
            _state.update { it.copy(isImageLoading = true) }
            removeUserImage()
                .onSuccess {
                    _state.update { it.copy(isImageLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isImageLoading = false, message = e.message) }
                }
        }
    }

    private fun updateUsername(name: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updateUsernameUseCase(name)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, message = "Name updated") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, message = e.message) }
                }
        }
    }

    private fun updatePassword(old: String, new: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updatePasswordUseCase(old, new)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, message = "Password updated") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, message = e.message) }
                }
        }
    }


    private fun updateUserImage(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isImageLoading = true) }

            val result = updateUserImageUseCase(uri)

            result
                .onSuccess { imagePath ->
                    Timber.d(">>> onSuccess called")
                    _state.update {
                        it.copy(
                            isImageLoading = false,
                            user = it.user?.copy(imgPath = imagePath)
                        )
                    }
                }
                .onFailure { e ->
                    Timber.e(">>> onFailure called: ${e.message}")
                    _state.update { it.copy(isImageLoading = false, message = e.message) }
                }
        }
    }
}

data class UserUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val isImageLoading: Boolean = false,
    val isEmailProvider: Boolean = false,
    val providerName: String? = null,
    val user: User? = null,
    val message: String? = null,
)