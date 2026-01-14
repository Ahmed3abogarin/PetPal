package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository
import com.vtol.petpal.presentation.register.AuthState
import kotlinx.coroutines.flow.Flow

class GetAuthState(
    private val repository: AuthRepository
) {
     operator fun invoke(): Flow<AuthState> {
        return repository.authState()
    }
}