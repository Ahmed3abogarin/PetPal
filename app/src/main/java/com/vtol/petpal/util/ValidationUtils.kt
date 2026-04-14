package com.vtol.petpal.util

import android.util.Patterns

object ValidationUtils {

    fun validateEmail(email: String): String? {
       return when {
            email.isEmpty() -> "Email can't be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password can't be empty"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.matches(".*[A-Z].*".toRegex()) -> "Password should contain at least one uppercase letter"
            else -> null
        }
    }

    fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> "Name can't be empty"
            name.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }
    }
}