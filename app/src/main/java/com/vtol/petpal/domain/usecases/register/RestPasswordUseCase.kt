package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class  RestPasswordUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.resetPassword(email)
    }
}