package com.vtol.petpal.presentation.register.signup

import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.presentation.register.GoogleAuthUiClient
import com.vtol.petpal.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val useCases: AuthUseCases,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = event.value,
                        nameError = ValidationUtils.validateName(event.value)
                    )
                }
            }

            is SignUpEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailError = ValidationUtils.validateEmail(event.value)
                    )
                }
            }

            is SignUpEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = ValidationUtils.validatePassword(event.value)
                    )
                }
            }

            is SignUpEvent.ErrorShown -> {
                _uiState.update { it.copy(error = null) }
            }

            is SignUpEvent.SignUpClicked -> register()
            is SignUpEvent.GoogleClicked -> signInWithGoogle(event.context)
            is SignUpEvent.FacebookClicked -> signInWithFacebook(event.token)
        }
    }

    private fun signInWithFacebook(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            useCases.signInWithFacebook(idToken)
                .onSuccess {
                    firebaseAnalyticsHelper.logEvent(
                        FirebaseAnalytics.Event.SIGN_UP, bundleOf(
                            FirebaseAnalytics.Param.METHOD to "facebook"
                        )
                    )

                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }


    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val token = googleAuthUiClient.signIn(context)

            if (token == null) {
                _uiState.update { it.copy(isLoading = false, error = "Something went wrong") }
                return@launch
            }

            useCases.signInWithGoogle(token)
                .onSuccess {
                    firebaseAnalyticsHelper.logEvent(
                        FirebaseAnalytics.Event.SIGN_UP, bundleOf(
                            FirebaseAnalytics.Param.METHOD to "google"
                        )
                    )

                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }


    fun register() = viewModelScope.launch {

        val nameError = ValidationUtils.validateName(_uiState.value.name)
        val emailError = ValidationUtils.validateEmail(_uiState.value.email)
        val passwordError = ValidationUtils.validatePassword(_uiState.value.password)

        if (nameError != null || emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, error = null) }

        useCases.signUp(
            User(name = _uiState.value.name, email = _uiState.value.email),
            _uiState.value.password
        ).onSuccess {
            firebaseAnalyticsHelper.logEvent(
                FirebaseAnalytics.Event.SIGN_UP, bundleOf(
                    FirebaseAnalytics.Param.METHOD to "email"
                )
            )
        }
            .onFailure { failure ->
                _uiState.update {
                    it.copy(isLoading = false, error = failure.message)
                }
            }
    }
}


data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)