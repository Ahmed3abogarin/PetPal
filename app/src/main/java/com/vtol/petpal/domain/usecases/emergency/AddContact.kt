package com.vtol.petpal.domain.usecases.emergency

import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.repository.EmergencyRepository

class AddContact(
    private val repository: EmergencyRepository
) {
    suspend operator fun invoke(contact: EmergencyContact) =
        repository.addContact(contact)
}