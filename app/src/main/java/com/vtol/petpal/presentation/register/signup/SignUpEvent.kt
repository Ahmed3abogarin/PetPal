package com.vtol.petpal.presentation.register.signup

sealed class SignUpEvent {
    data class NameChanged(val value: String) : SignUpEvent()
    data class EmailChanged(val value: String) : SignUpEvent()
    data class PasswordChanged(val value: String) : SignUpEvent()
    object SignUpClicked : SignUpEvent()
}