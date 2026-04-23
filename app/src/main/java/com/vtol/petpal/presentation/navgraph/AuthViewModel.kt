package com.vtol.petpal.presentation.navgraph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.usecases.register.AuthUseCases
import com.vtol.petpal.presentation.register.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: AuthUseCases
): ViewModel() {

    val authState: StateFlow<AuthState> =
        combine(
            useCases.getAuthState(),
            useCases.readAppEntry()
        ) { authState, onboardingCompleted ->
            if (!onboardingCompleted) {
                AuthState.OnBoarding
            } else {
                authState
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AuthState.Loading
        )


    fun completeOnBoarding() {
        viewModelScope.launch {
            useCases.saveAppEntry()
        }
    }
}