package com.vtol.petpal.presentation.register.login

import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.presentation.register.GoogleAuthUiClient
import com.vtol.petpal.util.AnalyticsParams
import com.vtol.petpal.util.ValidationUtils.validateEmail
import com.vtol.petpal.util.ValidationUtils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: AuthUseCases,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailError = validateEmail(event.value)
                    )
                }
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = validatePassword(event.value)
                    )
                }
            }

            is LoginEvent.ErrorShown -> {
                _uiState.update { it.copy(error = null) }
            }

            is LoginEvent.RestPasswordClicked -> restPassword()
            is LoginEvent.ClearForgotPasswordState -> clearForgotPasswordState()

            is LoginEvent.LoginClicked -> login()
            is LoginEvent.GoogleClicked -> signInWithGoogle(event.context)
            is LoginEvent.FacebookClicked -> signInWithFacebook(event.idToken)
        }
    }

    private fun restPassword() {
        viewModelScope.launch {
            val emailError = validateEmail(_uiState.value.email)

            if (emailError != null) {
                _uiState.update {
                    it.copy(emailError = emailError)
                }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true, error = null) }


            useCases.resetPassword(_uiState.value.email.trim())
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, forgetPasswordSuccess = true)
                    }

                }.onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
        }

    }

    private fun signInWithFacebook(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            useCases.signInWithFacebook(idToken)
                .onSuccess {
                    firebaseAnalyticsHelper.logEvent(
                        FirebaseAnalytics.Event.LOGIN, bundleOf(
                            AnalyticsParams.METHOD to "facebook"
                        )
                    )
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }

    private fun signInWithGoogle(context: Context) {
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
                        FirebaseAnalytics.Event.LOGIN, bundleOf(
                            AnalyticsParams.METHOD to "google"
                        )
                    )
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
        }
    }


    fun login() = viewModelScope.launch {

        val emailError = validateEmail(_uiState.value.email)
        val passwordError = validatePassword(_uiState.value.password)

        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val result = withContext(Dispatchers.IO) {
            useCases.signIn(_uiState.value.email.trim(), _uiState.value.password.trim())
        }

        result
            .onSuccess {
                firebaseAnalyticsHelper.logEvent(
                    FirebaseAnalytics.Event.LOGIN, bundleOf(
                        AnalyticsParams.METHOD to "email"
                    )
                )

                _uiState.update { it.copy(error = "Welcome back") }
            }
            .onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                Timber.tag("Failure").e(throwable)
            }
    }

    private fun clearForgotPasswordState() {
        _uiState.update {
            it.copy(
                forgetPasswordSuccess = false,
                forgotPasswordError = null
            )
        }
    }
}


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val forgetPasswordSuccess: Boolean = false,
    val forgotPasswordError: String? = null
)