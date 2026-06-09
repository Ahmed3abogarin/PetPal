package com.vtol.petpal.domain.usecases.emergency

data class EmergencyUseCases(
    val observeContacts: ObserveContacts,
    val addContact: AddContact,
    val updateContact: UpdateContact,
    val deleteContact: DeleteContact
)
