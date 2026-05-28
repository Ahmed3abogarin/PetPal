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
import androidx.compose.ui.tooling.preview.Preview
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun ChangeNameDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = BackgroundColor,
        onDismissRequest = {},
        title = { Text("Change Name") },
        text = {
            PetTextField(
                value = name,
                onValueChanged = { name = it },
                placeHolder = "Enter your name"
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) { Text(text = "Update", color = MainPurple) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = "Cancel", color = Color.Black) }
        }
    )
}


@Preview
@Composable
fun NameEditDialog() {
    PetPalTheme {
        ChangeNameDialog(
            onConfirm = {},
            onDismiss = {}
        )

    }
}