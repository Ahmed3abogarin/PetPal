package com.vtol.petpal.presentation.register

sealed class AuthEvent {
    data class NameChanged(val value: String) : AuthEvent()
    data class EmailChanged(val value: String) : AuthEvent()
    data class PasswordChanged(val value: String) : AuthEvent()
    object LoginClicked : AuthEvent()
    object RegisterClicked : AuthEvent()
}