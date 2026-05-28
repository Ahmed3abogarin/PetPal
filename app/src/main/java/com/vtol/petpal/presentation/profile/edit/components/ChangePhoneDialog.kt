package com.vtol.petpal.presentation.profile.edit.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.util.ValidationUtils

@Composable
fun ChangePhoneDialog(onDismiss: () -> Unit, onConfirm: (name: String) -> Unit) {

    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }


    AlertDialog(
        containerColor = BackgroundColor,
        onDismissRequest = { onDismiss() },
        title = { Text("Change Name") },
        text = {
            PetTextField(
                value = phone,
                onValueChanged = {
                    phone = it
                    phoneError = ValidationUtils.validatePhone(phone)
                },
                placeHolder = "+1 234 567 8900",
                error = phoneError
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(phone) },
                enabled = phoneError == null
            ) { Text(text = "Update", color = MainPurple) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = "Cancel", color = Color.Black) }
        }
    )
}