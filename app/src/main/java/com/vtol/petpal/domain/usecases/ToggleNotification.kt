package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.repository.UserPreferencesRepository

class ToggleNotification(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesRepository.setNotificationsEnabled(enabled)
    }
}