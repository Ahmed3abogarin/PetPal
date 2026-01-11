package com.vtol.petpal.domain.usecases.register

data class AuthUseCases(
    val signIn: SignIn,
    val signUp: Register,
    val logout: Logout,
    val getAuthState: GetAuthState
)
