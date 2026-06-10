package com.vtol.petpal.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ContactTypeMeta(
    val type: ContactType,
    val label: String,
    val icon: ImageVector,
    val color: Color,
)

val contactTypes = listOf(
    ContactTypeMeta(ContactType.PET_SITTER, "Pet Sitter", Icons.Outlined.Person, Color(0xFF8B5CF6)),
    ContactTypeMeta(
        ContactType.VETERINARIAN,
        "Veterinarian",
        Icons.Outlined.LocalHospital,
        Color(0xFF22C55E)
    ),
    ContactTypeMeta(
        ContactType.EMERGENCY_CLINIC,
        "Emergency Clinic",
        Icons.Outlined.MedicalServices,
        Color(0xFFEF4444)
    ),
    ContactTypeMeta(
        ContactType.FAMILY_MEMBER,
        "Family Member",
        Icons.Outlined.Groups,
        Color(0xFFF59E0B)
    ),
    ContactTypeMeta(
        ContactType.EMERGENCY_CONTACT,
        "Emergency",
        Icons.Outlined.Emergency,
        Color(0xFFEC4899)
    ),
)