package com.vtol.petpal.domain.model

data class EmergencyContact(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val relationship: ContactType = ContactType.EMERGENCY_CONTACT,
    val notes: String = "",
    val primary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class ContactType {
    VETERINARIAN,
    EMERGENCY_CLINIC,
    PET_SITTER,
    FAMILY_MEMBER,
    EMERGENCY_CONTACT
}