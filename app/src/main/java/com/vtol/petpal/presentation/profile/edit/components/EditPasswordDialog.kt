package com.vtol.petpal.presentation.profile.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.ValidationUtils

@Composable
fun EditPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (current: String, new: String) -> Unit
) {
    var current by remember { mutableStateOf("") }
    var currentError by remember { mutableStateOf<String?>(null) }
    var currentVisible by remember { mutableStateOf(false) }


    var newPw by remember { mutableStateOf("") }
    var newError by remember { mutableStateOf<String?>(null) }
    var newVisible by remember { mutableStateOf(false) }


    var confirm by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var confirmVisible by remember { mutableStateOf(false) }

    val passwordsMatch = newPw == confirm
    val isValid =
        current.isNotBlank() && ValidationUtils.validatePassword(newPw) == null && passwordsMatch

    AlertDialog(
        containerColor = BackgroundColor,
        onDismissRequest = onDismiss,
        title = { Text("Change password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PetTextField(
                    placeHolder = "Current password",
                    value = current,
                    visualTransformation = if (currentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (currentVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                    onValueChanged = {
                        current = it
                        ValidationUtils.validatePassword(current)
                    },
                    iconSize = 18.dp,
                    onTrailingClicked = { currentVisible = !currentVisible },
                    error = currentError
                )

                PetTextField(
                    placeHolder = "New password",
                    value = newPw,
                    visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (newVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                    onValueChanged = {},
                    iconSize = 18.dp,
                    onTrailingClicked = { newVisible = !newVisible },
                    error = newError
                )

                PetTextField(
                    placeHolder = "Confirm password",
                    value = confirm,
                    visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (confirmVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                    onValueChanged = {},
                    iconSize = 18.dp,
                    onTrailingClicked = { confirmVisible = !confirmVisible },
                    error = confirmError ?: if (!passwordsMatch) "Passwords don't match" else null
                )

            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(current, newPw) },
                enabled = isValid
            ) { Text(text = "Update", color = MainPurple) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = "Cancel", color = Color.Black) }
        }
    )
}


@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        EditPasswordDialog(
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}