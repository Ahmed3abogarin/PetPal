package com.vtol.petpal.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun login(email: String, password: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        useCases.signIn(email, password)
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
            .onFailure { _uiState.update { it.copy(isLoading = false, error = it.error) } }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        useCases.signUp(email, password)
            .onSuccess { _uiState.update { it.copy(isLoading = false) } }
            .onFailure { _uiState.update { it.copy(isLoading = false, error = it.error) } }
    }

    fun logout() = useCases.logout()

}


data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)