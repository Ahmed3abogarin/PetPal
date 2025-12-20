package com.vtol.petpal.presentation.home.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {

    // Get today in millis
    val todayMillis = remember {
        LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    // Restrict selectable dates to today or earlier
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis < todayMillis // only past & today allowed
            }
        }
    )


    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TaskDatePicker(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {

    // Restrict selectable dates to today and onwards
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Only allow dates from today onwards
                return utcTimeMillis >= System.currentTimeMillis() - 86400000 // Subtract 24h buffer for safety
            }
        }
    )


    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millis = datePickerState.selectedDateMillis
                millis?.let {
                    val date = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC")) // DatePicker uses UTC
                        .toLocalDate()
                    onDateSelected(date)
                    onDismiss()
                }


            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}