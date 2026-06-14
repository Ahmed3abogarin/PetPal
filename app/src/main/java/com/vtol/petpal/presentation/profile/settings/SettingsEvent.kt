package com.vtol.petpal.presentation.profile.settings

sealed class SettingsEvent {
    data class OnToggleNotification(val isEnabled: Boolean): SettingsEvent()
    data class OnToggleSyncUp(val isEnabled: Boolean): SettingsEvent()
}