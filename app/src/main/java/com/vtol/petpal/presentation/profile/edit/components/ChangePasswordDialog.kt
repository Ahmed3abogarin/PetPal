package com.vtol.petpal.presentation.profile.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.ValidationUtils

@Composable
fun ChangePasswordDialog(
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

    val isValid = currentError == null && newError == null && confirmError == null
            && current.isNotBlank() && newPw.isNotBlank() && confirm.isNotBlank()

    AlertDialog(
        containerColor = BackgroundColor,
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ExtraLightPurple)
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(14.dp)
                            .size(24.dp),
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = MainPurple
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Change password", lineHeight = 16.sp, fontSize = 16.sp)
                    Text(
                        text = "Choose a strong password to keep your account secure",
                        lineHeight = 16.sp,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PetTextField(
                    placeHolder = "Current password",
                    value = current,
                    visualTransformation = if (currentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (currentVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                    onValueChanged = {
                        current = it
                        currentError = ValidationUtils.validatePassword(it)
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
                    onValueChanged = {
                        newPw = it
                        newError = when {
                            ValidationUtils.validatePassword(it) != null -> ValidationUtils.validatePassword(
                                it
                            )

                            it == current -> "New password can't be the same as current"
                            else -> null
                        }
                    },
                    iconSize = 18.dp,
                    onTrailingClicked = { newVisible = !newVisible },
                    error = newError
                )

                PetTextField(
                    placeHolder = "Confirm password",
                    value = confirm,
                    visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (confirmVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                    onValueChanged = {
                        confirm = it
                        confirmError = if (it != newPw) "Passwords don't match" else null
                    },
                    iconSize = 18.dp,
                    onTrailingClicked = { confirmVisible = !confirmVisible },
                    error = confirmError
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
        ChangePasswordDialog(
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}