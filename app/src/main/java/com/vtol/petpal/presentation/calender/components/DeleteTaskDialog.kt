package com.vtol.petpal.presentation.calender.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DeleteTaskDialog(
    isRecurring: Boolean,
    onDeleteThis: () -> Unit,
    onDeleteAll: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isRecurring) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Delete Task") },
            text = { Text("Do you want to delete only this occurrence, or all future events as well?") },
            confirmButton = {
                TextButton(onClick = onDeleteAll) { Text("All events") }
            },
            dismissButton = {
                TextButton(onClick = onDeleteThis) { Text("This event") }
            }
        )
    } else {
        // non-recurring, just delete directly without dialog
        LaunchedEffect(Unit) { onDeleteAll() }
    }
}