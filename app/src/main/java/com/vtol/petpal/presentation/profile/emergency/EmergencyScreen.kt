package com.vtol.petpal.presentation.profile.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.profile.emergency.components.AddContactBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.ContactDetailsBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.ContactOptionsBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.DeleteContactBottomSheet
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.ShareManager.openDialer
import com.vtol.petpal.util.ShareManager.shareContact

@Composable
fun EmergencyScreen(
    state: EmergencyUiState,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    var currentSheet by remember {
        mutableStateOf<EmergencySheet?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .background(petPalGradient)
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 16.dp)
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton { navigateUp() }


            Text(
                text = "Emergency Contacts",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            AppIconButton(
                icon = R.drawable.ic_add,
            ) { currentSheet = EmergencySheet.Add }
        }
        Text(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
            text = "Korem ipsum dolor sit amet, consectetur adipiscing elit,Korem ipsum dolor sit amet, consectetur adipiscing elit.",
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.contacts) {
                EmergencyContactCard(
                    contact = it,
                    onCallClick = { openDialer(context, it.phoneNumber) },
                    onMoreClick = { currentSheet = EmergencySheet.More(it) },
                    navigateToDetails = { currentSheet = EmergencySheet.Details(it) }
                )
            }
        }
    }

    when (val sheet = currentSheet) {
        is EmergencySheet.Add -> {
            AddContactBottomSheet(
                onSave = { newContact -> /* viewModel.add(newContact) */ },
                onDismiss = { currentSheet = null }
            )
        }

        is EmergencySheet.Edit -> {
            AddContactBottomSheet(
                initial = sheet.contact,          // reuses the same sheet
                onSave = { updated -> /* viewModel.update(updated) */ },
                onDismiss = { currentSheet = null }
            )
        }

        is EmergencySheet.More -> {
            ContactOptionsBottomSheet(
                sheet.contact,
                onDismiss = { currentSheet = null },
                onView = { currentSheet = EmergencySheet.Details(sheet.contact) },
                onEdit = { currentSheet = EmergencySheet.Edit(sheet.contact) },  // ← wire edit
                onDelete = { currentSheet = EmergencySheet.Delete(sheet.contact) },
                onShare = { shareContact(context, sheet.contact)}
            )
        }

        is EmergencySheet.Delete -> {
            DeleteContactBottomSheet(
                contactName = sheet.contact.name,
                onDismiss = { currentSheet = null },
                onConfirm = { /* viewModel.delete(sheet.contact) */ }
            )
        }

        is EmergencySheet.Details -> {
            ContactDetailsBottomSheet(
                contact = sheet.contact,
                onEdit = { currentSheet = EmergencySheet.Edit(sheet.contact) },
                onCall = { openDialer(context, sheet.contact.phoneNumber) },
                onDismiss = { currentSheet = null }
            )
        }

        null -> Unit
    }
}


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

@Preview
@Composable
fun EmergencyPreview() {
    PetPalTheme {
        EmergencyScreen(
            state = EmergencyUiState(),
            navigateUp = {}
        )
    }
}