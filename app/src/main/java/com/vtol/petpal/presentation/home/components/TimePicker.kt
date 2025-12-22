package com.vtol.petpal.presentation.home.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePicker(modifier: Modifier = Modifier,onTimeChanged: (LocalTime) -> Unit) {

    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val now = LocalDateTime.now()


    var selectedTime by remember {
        mutableStateOf(
            now.toLocalTime().withSecond(0).withNano(0)
        )
    }


    TextField(
        value = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.White,
            focusedContainerColor = Color.White,
            disabledIndicatorColor = Color.Transparent
        ),
        onValueChange = {},
        label = { Text("Due time") },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
//                        focusManger.clearFocus()
                showTimePicker = true
            },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = Color.Black
            )
        }
    )

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
                onTimeChanged(selectedTime)
            showTimePicker = false
            },
            now.hour,
            now.minute,
            true
        ).show()

    }


}