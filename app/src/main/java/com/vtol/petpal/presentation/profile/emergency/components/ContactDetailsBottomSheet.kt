package com.vtol.petpal.presentation.profile.emergency.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

// ─── Type metadata (same source of truth as other sheets) ────────────────────

private data class TypeMeta(
    val label: String,
    val icon: ImageVector,
    val color: Color,
)

private fun ContactType.meta() = when (this) {
    ContactType.PET_SITTER -> TypeMeta(
        "Pet Sitter", Icons.Outlined.Person, Color(0xFF8B5CF6)
    )
    ContactType.VETERINARIAN -> TypeMeta(
        "Veterinarian", Icons.Outlined.LocalHospital, Color(0xFF22C55E)
    )
    ContactType.EMERGENCY_CLINIC -> TypeMeta(
        "Emergency Clinic", Icons.Outlined.MedicalServices, Color(0xFFEF4444)
    )
    ContactType.FAMILY_MEMBER -> TypeMeta(
        "Family Member", Icons.Outlined.Groups, Color(0xFFF59E0B)
    )
    ContactType.EMERGENCY_CONTACT -> TypeMeta(
        "Emergency Contact", Icons.Outlined.Emergency, Color(0xFFEC4899)
    )
}

// ─── Sheet content (previewable) ─────────────────────────────────────────────

@Composable
fun ContactDetailsSheetContent(
    contact: EmergencyContact,
    onEdit: () -> Unit,
    onCall: (String) -> Unit
) {
    val meta = contact.relationship.meta()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 36.dp)
    ) {
        // ── Drag handle


        // ── Hero header with gradient band
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            meta.color.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
                .padding(top = 24.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(meta.color.copy(alpha = 0.15f))
                        .border(2.dp, meta.color.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = meta.icon,
                        contentDescription = null,
                        tint = meta.color,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Name + primary badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = contact.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                    if (contact.isPrimary) {
                        Spacer(Modifier.width(8.dp))
                        PrimaryBadge()
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Type pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(meta.color.copy(alpha = 0.12f))
                        .border(1.dp, meta.color.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = meta.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = meta.color
                    )
                }
            }
        }

        // ── Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Call — primary action
            Button(
                onClick = { onCall(contact.phoneNumber) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = meta.color)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Call", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            // Edit — secondary
            OutlinedButton(
                onClick = { onEdit() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(listOf(MainPurple, MainPurple))
                ),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MainPurple)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Edit", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Info cards
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailRow(
                icon = Icons.Outlined.Phone,
                label = "Phone number",
                value = contact.phoneNumber,
                accentColor = meta.color
            )

            DetailRow(
                icon = meta.icon,
                label = "Relationship",
                value = meta.label,
                accentColor = meta.color
            )

            if (contact.notes.isNotBlank()) {
                DetailRow(
                    icon = Icons.AutoMirrored.Outlined.Notes,
                    label = "Notes",
                    value = contact.notes,
                    accentColor = meta.color
                )
            }

            DetailRow(
                icon = Icons.Outlined.Star,
                label = "Priority",
                value = if (contact.isPrimary) "Primary contact" else "Standard contact",
                accentColor = if (contact.isPrimary) MainPurple else Color(0xFF9CA3AF),
                valueColor = if (contact.isPrimary) MainPurple else Color(0xFF6B7280)
            )
        }
    }
}

// ─── ModalBottomSheet wrapper ─────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsBottomSheet(
    contact: EmergencyContact,
    onEdit: () -> Unit,
    onCall: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        dragHandle = null,
        tonalElevation = 0.dp,
    ) {
        ContactDetailsSheetContent(
            contact   = contact,
            onEdit    = onEdit,
            onCall    = onCall,
        )
    }
}

// ─── Sub-components ───────────────────────────────────────────────────────────

@Composable
private fun PrimaryBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                )
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "★ Primary",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    valueColor: Color = Color(0xFF111111),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF9FAFB))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9CA3AF),
                letterSpacing = 0.4.sp
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = valueColor,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun ContactDetailsPreview() {
    PetPalTheme {
        ContactDetailsSheetContent(
            contact = EmergencyContact(
                id           = "1",
                name         = "Dr. Emma Johnson",
                phoneNumber  = "+966 55 123 4567",
                relationship = ContactType.VETERINARIAN,
                notes        = "Available Mon–Fri, 9am–6pm. Clinic on King Fahd Road.",
                isPrimary    = true
            ),
            onEdit    = {},
            onCall    = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun ContactDetailsNoNotesPreview() {
    PetPalTheme {
        ContactDetailsSheetContent(
            contact = EmergencyContact(
                id           = "2",
                name         = "Sara Al-Otaibi",
                phoneNumber  = "+966 50 987 6543",
                relationship = ContactType.PET_SITTER,
                notes        = "",
                isPrimary    = false
            ),
            onEdit    = {},
            onCall    = {},
        )
    }
}