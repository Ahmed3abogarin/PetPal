package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class SignIn(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.login(email, password)
    }
}