package com.vtol.petpal.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : ViewModel() {

    val authState: StateFlow<AuthState> = useCases.getAuthState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AuthState.Loading
    )


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState


    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged ->
                _uiState.update { it.copy(user = _uiState.value.user.copy(email = event.value)) }

            is AuthEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value) }

            is AuthEvent.NameChanged ->
                _uiState.update { it.copy(user = _uiState.value.user.copy(name = event.value)) }

            AuthEvent.LoginClicked -> login()
            AuthEvent.RegisterClicked -> register()

        }
    }



    fun login() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        useCases.signIn(_uiState.value.user.email, _uiState.value.password)
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
            .onFailure { _uiState.update { it.copy(isLoading = false, error = it.error) } }
    }

    fun register() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        useCases.signUp(_uiState.value.user, _uiState.value.password)
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
            .onFailure { _uiState.update { it.copy(isLoading = false, error = it.error) } }
    }

    fun logout() = useCases.logout()

}


data class AuthUiState(
    val user: User = User(),
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)