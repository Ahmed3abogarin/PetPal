package com.vtol.petpal.presentation.profile.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vtol.petpal.domain.usecases.emergency.EmergencyEvent
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.profile.emergency.components.AddContactBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.ContactDetailsBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.ContactOptionsBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.DeleteContactBottomSheet
import com.vtol.petpal.presentation.profile.emergency.components.EmergencyContactCard
import com.vtol.petpal.presentation.profile.emergency.components.EmptyContactsScreen
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.Gold
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.TextPurple
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.ShareManager.openDialer
import com.vtol.petpal.util.ShareManager.shareContact
import com.vtol.petpal.util.showToast

@Composable
fun EmergencyScreen(
    modifier: Modifier = Modifier,
    state: EmergencyUiState,
    currentSheet: EmergencySheet?,
    navigateUp: () -> Unit,
    event: (EmergencyEvent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(state.message) {
        if (state.message != null) {
            context.showToast(text = state.message)
            event(EmergencyEvent.ErrorShown)
        }
    }

    LaunchedEffect(Unit) {
        event(EmergencyEvent.LogScreenView)
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
            ) { event(EmergencyEvent.OpenAdd) }
        }
        Text(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
            text = "People you can count on in an emergency",
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val primary = state.contacts.filter { it.primary }
            val others = state.contacts.filter { !it.primary }

            if (primary.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Gold)
                        )
                        Text(
                            text = "Primary",
                            color = TextPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                items(primary) {
                    EmergencyContactCard(
                        contact = it,
                        onCallClick = { openDialer(context, it.phoneNumber) },
                        onMoreClick = { event(EmergencyEvent.OpenMore(it)) },
                        navigateToDetails = { event(EmergencyEvent.OpenDetails(it)) }
                    )

                }
            }

            if (others.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Other contacts",
                        color = TextPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                items(others) {
                    EmergencyContactCard(
                        contact = it,
                        onCallClick = { openDialer(context, it.phoneNumber) },
                        onMoreClick = { event(EmergencyEvent.OpenMore(it)) },
                        navigateToDetails = { event(EmergencyEvent.OpenDetails(it)) }
                    )
                }
            }
        }
    }
    if (state.contacts.isEmpty()) {
        EmptyContactsScreen { event(EmergencyEvent.OpenAdd) }
    }

    if (state.isLoading) {
        LoadingIndicator()
    }

    when (currentSheet) {
        is EmergencySheet.Add -> {
            AddContactBottomSheet(
                isLoading = state.isSheetLoading,
                nameError = state.nameError,
                phoneError = state.phoneError,
                onSave = { newContact -> event(EmergencyEvent.AddContact(newContact)) },
                onDismiss = { event(EmergencyEvent.DismissSheet) }
            )
        }

        is EmergencySheet.Edit -> {
            // reuses the same sheet ;)
            AddContactBottomSheet(
                isLoading = state.isSheetLoading,
                nameError = state.nameError,
                phoneError = state.phoneError,
                initial = currentSheet.contact,
                onSave = { updated -> event(EmergencyEvent.UpdateContact(updated)) },
                onDismiss = { event(EmergencyEvent.DismissSheet) }
            )
        }

        is EmergencySheet.More -> {
            ContactOptionsBottomSheet(
                currentSheet.contact,
                onDismiss = { event(EmergencyEvent.DismissSheet) },
                onView = { event(EmergencyEvent.OpenDetails(currentSheet.contact)) },
                onEdit = { event(EmergencyEvent.OpenEdit(currentSheet.contact)) },
                onDelete = { event(EmergencyEvent.OpenDelete(currentSheet.contact)) },
                onShare = { shareContact(context, currentSheet.contact) }
            )
        }

        is EmergencySheet.Delete -> {
            DeleteContactBottomSheet(
                contactName = currentSheet.contact.name,
                onDismiss = { event(EmergencyEvent.DismissSheet) },
                onConfirm = { event(EmergencyEvent.DeleteContact(currentSheet.contact)) }
            )
        }

        is EmergencySheet.Details -> {
            ContactDetailsBottomSheet(
                contact = currentSheet.contact,
                onEdit = { event(EmergencyEvent.OpenEdit(currentSheet.contact)) },
                onCall = { openDialer(context, currentSheet.contact.phoneNumber) },
                onDismiss = { event(EmergencyEvent.DismissSheet) }
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
fun EmergencyPreview() {
    PetPalTheme {
        EmergencyScreen(
            state = EmergencyUiState(),
            navigateUp = {},
            event = {},
            currentSheet = null
        )
    }
}