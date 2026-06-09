package com.vtol.petpal.presentation.profile.emergency

import com.vtol.petpal.domain.model.EmergencyContact

sealed interface EmergencySheet {

    data class Details(
        val contact: EmergencyContact
    ) : EmergencySheet

    data class Edit(
        val contact: EmergencyContact
    ) : EmergencySheet

    data class More(
        val contact: EmergencyContact
    ) : EmergencySheet

    object Add: EmergencySheet
    data class Delete(
        val contact: EmergencyContact
    ) : EmergencySheet
}