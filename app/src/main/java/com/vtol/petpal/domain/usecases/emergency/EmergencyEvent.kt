package com.vtol.petpal.domain.usecases.emergency

import com.vtol.petpal.domain.model.EmergencyContact

sealed class EmergencyEvent {

    // Sheet navigation
    data object OpenAdd : EmergencyEvent()
    data class OpenMore(val contact: EmergencyContact) : EmergencyEvent()
    data class OpenEdit(val contact: EmergencyContact) : EmergencyEvent()
    data class OpenDelete(val contact: EmergencyContact) : EmergencyEvent()
    data class OpenDetails(val contact: EmergencyContact) : EmergencyEvent()
    data object DismissSheet : EmergencyEvent()



    // CRUD
    data class AddContact(val contact: EmergencyContact) : EmergencyEvent()
    data class UpdateContact(val contact: EmergencyContact) : EmergencyEvent()
    data class DeleteContact(val contact: EmergencyContact) : EmergencyEvent()
    object ErrorShown: EmergencyEvent()
}