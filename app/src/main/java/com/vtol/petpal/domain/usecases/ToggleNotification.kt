package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.repository.NotificationRepository

class ToggleNotification(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        notificationRepository.setNotificationsEnabled(enabled)
    }
}