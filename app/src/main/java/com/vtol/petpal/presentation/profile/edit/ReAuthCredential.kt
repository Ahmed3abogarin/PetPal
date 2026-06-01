package com.vtol.petpal.presentation.profile.edit

import com.google.firebase.auth.AuthCredential

sealed class ReAuthCredential {
    data class Email(val password: String) : ReAuthCredential()
    data class Social(val credential: AuthCredential) : ReAuthCredential() // covers both
}