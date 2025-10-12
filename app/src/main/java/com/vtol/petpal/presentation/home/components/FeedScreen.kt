package com.vtol.petpal.presentation.home.components

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.FeedType
import com.vtol.petpal.domain.model.Recurrence
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.formatDate
import java.time.LocalDate

@Composable
fun FeedScreen() {

    var selectedType by remember { mutableStateOf(FeedType.Lunch) }

    var birthDate by remember { mutableStateOf<LocalDate?>(null) }

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }


    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            birthDate = LocalDate.of(year, month + 1, day)
        },
        LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0XFFFFF0DB))
    ) {
        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = ""
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.ic_feed),
                contentDescription = ""
            )
            Text(text = "Add Feed", style = MaterialTheme.typography.headlineLarge)

            TextField(
                value = "Pet",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeedTypeButton(
                    modifier = Modifier.weight(1f),
                    feedType = FeedType.Breakfast.name,
                    isSelected = selectedType == FeedType.Breakfast,
                    onClick = { selectedType = FeedType.Breakfast }
                )
                FeedTypeButton(
                    modifier = Modifier.weight(1f),
                    feedType = FeedType.Lunch.name,
                    isSelected = selectedType == FeedType.Lunch,
                    onClick = { selectedType = FeedType.Lunch }
                )
                FeedTypeButton(
                    modifier = Modifier.weight(1f),
                    feedType = FeedType.Dinner.name,
                    isSelected = selectedType == FeedType.Dinner,
                    onClick = { selectedType = FeedType.Dinner }
                )

            }
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Food") },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                )
            )

            // Time
            OutlinedTextField(
                value = birthDate?.formatDate() ?: "",
                onValueChange = {},
                label = { Text("Time") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePicker.show() },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )

            // ******** Recurrence **********
            var selectedRecurrence by remember { mutableStateOf(Recurrence.NONE) }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Recurrence.entries.forEach { option ->
                    DropdownMenuItem(
                        onClick = { selectedRecurrence = option },
                        text = { Text(text = option.name) })
                }
            }

        }

    }

}

@Composable
fun FeedTypeButton(
    modifier: Modifier = Modifier,
    feedType: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0XFF6F7073) else Color(
                0XFFFFE2B9
            )
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp),
            text = feedType,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Preview
@Composable
fun FeedPreview() {
    PetPalTheme {
        FeedScreen()
    }
}