package com.vtol.petpal.presentation.profile.edit

sealed class EditProfileDialog {
    object Username : EditProfileDialog()
    object Phone : EditProfileDialog()
    object Password : EditProfileDialog()
    object Delete : EditProfileDialog()
    object None: EditProfileDialog()
}