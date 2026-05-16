package com.vtol.petpal.presentation.register.signup

import android.content.Context

sealed class SignUpEvent {
    data class NameChanged(val value: String) : SignUpEvent()
    data class EmailChanged(val value: String) : SignUpEvent()
    data class PasswordChanged(val value: String) : SignUpEvent()
    object SignUpClicked : SignUpEvent()
    data class GoogleClicked(val context: Context) : SignUpEvent()
    data class FacebookClicked(val token: String) : SignUpEvent()
}