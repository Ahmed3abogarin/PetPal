package com.vtol.petpal.presentation.profile.edit

import android.content.Context
import android.net.Uri

sealed class EditEvents {
    object RemoveImage: EditEvents()
    object ErrorShown: EditEvents()

    object LogScreenView: EditEvents()
    data class ReAuthWithGoogle(val context: Context): EditEvents()
    data class UpdateImage(val uri: Uri): EditEvents()
    data class UpdateUsername(val name: String) : EditEvents()
    data class UpdatePhone(val phone: String) : EditEvents()
    data class UpdatePassword(val old: String, val new: String) : EditEvents()

    data class DeleteAccount(val credential: ReAuthCredential) : EditEvents()
}