package com.vtol.petpal.presentation.profile.emergency.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.Red

private val DividerColor  = Color(0xFFEEEEEE)
private val DeleteRedBg   = Color(0xFFFFF0F0)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactOptionsBottomSheet(
    contact: EmergencyContact,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onView: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDDDDD))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Contact header
            ContactHeader(contact)

            Spacer(Modifier.height(20.dp))

            // Action items
            OptionItem(
                icon = Icons.Default.Edit,
                iconTint = MainPurple,
                label = "Edit Contact",
                onClick = { onEdit() }
            )
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            OptionItem(
                icon = Icons.Default.Info,
                iconTint = MainPurple,
                label = "View Details",
                onClick = { onView()}
            )
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            OptionItem(
                icon = Icons.Default.Share,
                iconTint = MainPurple,
                label = "Share Contact",
                onClick = { onShare() }
            )
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            OptionItem(
                icon = Icons.Default.Delete,
                iconTint = Red,
                label = "Delete Contact",
                labelColor = Red,
                background = DeleteRedBg,
                onClick = { onDelete() }
            )
        }
    }
}

@Composable
private fun ContactHeader(contact: EmergencyContact) {
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Avatar
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

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = contact.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    // Shield badge icon — swap with your actual badge drawable if needed
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MainPurple,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = contact.phoneNumber,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun OptionItem(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    labelColor: Color = Color.Black,
    background: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = labelColor
        )
    }
}