package com.vtol.petpal.domain.usecases.emergency

import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.repository.EmergencyRepository

class UpdateContact(
    private val repository: EmergencyRepository
) {
    suspend operator fun invoke(contact: EmergencyContact) =
        repository.updateContact(contact)
}