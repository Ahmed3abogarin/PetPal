package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.repository.NotificationRepository

class GetNotificationStatus(
    private val repository: NotificationRepository
) {
    operator fun invoke() =
        repository.isNotificationsEnabled()
}