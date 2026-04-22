package com.vtol.petpal.presentation.profile

sealed class ProfileEvents {
    data object SignOut: ProfileEvents()
}
