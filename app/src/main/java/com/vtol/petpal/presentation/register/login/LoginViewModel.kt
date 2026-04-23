package com.vtol.petpal.presentation.register.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.usecases.register.AuthUseCases
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
    private val useCases: AuthUseCases
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

            is LoginEvent.LoginClicked -> login()
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
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
            .onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                Timber.tag("Failure").e(throwable)
            }
    }
}


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)