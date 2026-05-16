package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class SignInWithFacebook(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(idToken: String): Result<Unit> =
        authRepository.signInWithFacebook(idToken)
}