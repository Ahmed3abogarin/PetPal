package com.vtol.petpal.presentation.register

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object OnBoarding: AuthState()
    object Loading : AuthState()                   // Initial/loading state

    object Unauthenticated : AuthState()          // User not logged in

    data class Authenticated(val user: FirebaseUser) : AuthState()  // User logged in

    data class Error(val message: String) : AuthState()
}