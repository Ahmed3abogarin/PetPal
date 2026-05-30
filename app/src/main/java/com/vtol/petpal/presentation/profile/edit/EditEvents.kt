package com.vtol.petpal.presentation.profile.edit

import android.net.Uri

sealed class EditEvents {
    object RemoveImage: EditEvents()
    data class UpdateImage(val uri: Uri): EditEvents()
}