package com.vtol.petpal.presentation.profile

import android.net.Uri

sealed class ProfileEvents {
    object SignOut: ProfileEvents()
    data class UpdateImage(val uri: Uri): ProfileEvents()
}
