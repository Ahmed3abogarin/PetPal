package com.vtol.petpal.domain.usecases.emergency

import com.vtol.petpal.domain.repository.EmergencyRepository

class ObserveContacts(
    private val repository: EmergencyRepository
) {
    operator fun invoke() =
        repository.observeContacts()
}