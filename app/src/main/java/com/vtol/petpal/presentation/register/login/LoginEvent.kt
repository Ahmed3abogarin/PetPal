package com.vtol.petpal.presentation.register.login

import android.content.Context

sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    data class GoogleClicked(val context: Context): LoginEvent()
    data class FacebookClicked(val idToken: String) : LoginEvent()
}
