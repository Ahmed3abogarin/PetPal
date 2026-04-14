package com.vtol.petpal.presentation.register.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.value) }
                ValidationUtils.validateName(event.value)?.let { error ->
                    _uiState.update { it.copy(nameError = error) }
                }

            }

            is SignUpEvent.EmailChanged -> {

            }
            is SignUpEvent.PasswordChanged -> {

            }
            is SignUpEvent.SignUpClicked -> {}
        }
    }


    fun register() = viewModelScope.launch {

        if (uiState.value.email.isEmpty() || uiState.value.name.isEmpty() || uiState.value.password.isEmpty()) {
            _uiState.update { it.copy(error = "Fields cannot be empty") }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, error = null) }


        useCases.signUp(
            User(name = _uiState.value.name, email = _uiState.value.email), _uiState.value.password
        )
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
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