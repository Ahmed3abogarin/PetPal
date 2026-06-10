package com.vtol.petpal.presentation.profile.emergency.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.ContactTypeMeta
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.model.contactTypes
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun AddContactSheetContent(
    initial: EmergencyContact = EmergencyContact(),
    isLoading: Boolean,
    nameError: String? = null,
    phoneError: String? = null,
    onSave: (EmergencyContact) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(initial.name) }
    var phone by remember { mutableStateOf(initial.phoneNumber) }
    var notes by remember { mutableStateOf(initial.notes) }
    var relationship by remember { mutableStateOf(initial.relationship) }
    var isPrimary by remember { mutableStateOf(initial.primary) }


    val isEdit = initial.id.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 36.dp)
    ) {
        // ── Drag handle
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp, bottom = 20.dp)
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0xFFDDDDDD))
        )

        // ── Title row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = if (isEdit) "Edit Contact" else "New Contact",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )
                Text(
                    text = "People you can count on in an emergency.",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Contact type selector
        SectionLabel("Contact type")
        Spacer(Modifier.height(10.dp))
        ContactTypeGrid(
            selected = relationship,
            onSelect = { relationship = it }
        )

        Spacer(Modifier.height(20.dp))

        // ── Name
        SectionLabel("Full name")
        Spacer(Modifier.height(8.dp))
        PetPalTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "e.g. Emma Johnson",
            errorMessage = nameError,
            leadingIcon = Icons.Outlined.Person,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(Modifier.height(16.dp))

        // ── Phone
        SectionLabel("Phone number")
        Spacer(Modifier.height(8.dp))
        PetPalTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = "+966 55 123 4567",
            errorMessage = phoneError,
            leadingIcon = Icons.Outlined.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(Modifier.height(16.dp))

        // ── Notes
        SectionLabel("Notes  (optional)")
        Spacer(Modifier.height(8.dp))
        PetPalTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = "Availability, address, anything helpful…",
            leadingIcon = Icons.AutoMirrored.Outlined.Notes,
            singleLine = false,
            minLines = 3,
        )

        Spacer(Modifier.height(20.dp))

        // ── Primary toggle
        PrimaryToggleRow(
            checked = isPrimary,
            onCheckedChange = { isPrimary = it }
        )

        Spacer(Modifier.height(28.dp))

        // ── Save button
        Button(
            onClick = {
                onSave(
                    initial.copy(
                        name = name.trim(),
                        phoneNumber = phone.trim(),
                        relationship = relationship,
                        notes = notes.trim(),
                        primary = isPrimary
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = if (isEdit) "Save changes" else "Add contact",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cancel",
                fontSize = 15.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

// ─── ModalBottomSheet wrapper ─────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactBottomSheet(
    initial: EmergencyContact = EmergencyContact(),
    nameError: String? = null,
    phoneError: String? = null,
    isLoading: Boolean,
    onSave: (EmergencyContact) -> Unit,
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
        AddContactSheetContent(
            initial = initial,
            nameError = nameError,
            phoneError = phoneError,
            isLoading = isLoading,
            onSave = onSave,
            onDismiss = onDismiss
        )
    }
}

// ─── Sub-components ───────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF6B7280),
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun ContactTypeGrid(
    selected: ContactType,
    onSelect: (ContactType) -> Unit,
) {
    // Two rows: 3 items, then 2 centred
    val rows = listOf(contactTypes.take(3), contactTypes.drop(3))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // If last row has fewer items, add weight spacers so chips center
                if (row.size < 3) Spacer(Modifier.weight(0.5f))
                row.forEach { meta ->
                    ContactTypeChip(
                        meta = meta,
                        isSelected = selected == meta.type,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelect(meta.type) }
                    )
                }
                if (row.size < 3) Spacer(Modifier.weight(0.5f))
            }
        }
    }
}

@Composable
private fun ContactTypeChip(
    meta: ContactTypeMeta,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) meta.color.copy(alpha = 0.12f) else Color(0xFFF9FAFB),
        animationSpec = tween(200), label = "chip_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) meta.color else Color(0xFFE5E7EB),
        animationSpec = tween(200), label = "chip_border"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp, horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(meta.color.copy(alpha = if (isSelected) 0.18f else 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = meta.icon,
                contentDescription = meta.label,
                tint = meta.color,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = meta.label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) meta.color else Color(0xFF6B7280),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 13.sp
        )
    }
}

@Composable
private fun PetPalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val isError = errorMessage != null
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            placeholder = {
                Text(placeholder, color = Color(0xFFD1D5DB), fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error else Color(0xFF9CA3AF),
                    modifier = Modifier.size(18.dp)
                )
            },
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainPurple,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFAFAFA),
                errorContainerColor = Color(0xFFFFF5F5),
                cursorColor = MainPurple,
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color(0xFF111111))
        )
        if (isError && errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun AddContactSheetPreview() {
    PetPalTheme {
        AddContactSheetContent(
            onSave = {},
            onDismiss = {}, isLoading = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
fun EditContactSheetPreview() {
    PetPalTheme {
        AddContactSheetContent(
            initial = EmergencyContact(
                id = "abc123",
                name = "Dr. Emma Johnson",
                phoneNumber = "+966 55 123 4567",
                relationship = ContactType.VETERINARIAN,
                notes = "Available Mon–Fri, 9am–6pm",
                primary = true
            ),
            isLoading = false,
            onSave = {},
            onDismiss = {}
        )
    }
}