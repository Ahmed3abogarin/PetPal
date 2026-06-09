package com.vtol.petpal.presentation.profile.emergency.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact

@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    modifier: Modifier = Modifier,
    navigateToDetails: () -> Unit,
    onCallClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val (icon, color, title) = when (contact.relationship) {
        ContactType.PET_SITTER -> Triple(
            Icons.Outlined.Person,
            Color(0xFF8B5CF6),
            "Pet Sitter"
        )

        ContactType.VETERINARIAN -> Triple(
            Icons.Outlined.LocalHospital,
            Color(0xFF22C55E),
            "Veterinarian"
        )

        ContactType.EMERGENCY_CLINIC -> Triple(
            Icons.Outlined.MedicalServices,
            Color(0xFFEF4444),
            "Emergency Clinic"
        )

        ContactType.FAMILY_MEMBER -> Triple(
            Icons.Outlined.Groups,
            Color(0xFFEAB308),
            "Emergency Contact"
        )

        ContactType.EMERGENCY_CONTACT -> Triple(
            Icons.Outlined.Emergency,
            Color(0xFFEA0808),
            "Emergency Contact"
        )
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { navigateToDetails() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = .15f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = color
                )

                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = contact.phoneNumber,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            FilledIconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = color.copy(alpha = 0.1f)),
                onClick = {
                    onCallClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                    tint = color
                )
            }
            Icon(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onMoreClick() },
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
    }
}