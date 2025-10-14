package com.vtol.petpal.presentation.home.components

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                .fillMaxWidth()
                .align(Alignment.TopCenter),
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
            var selectedRecurrence by remember { mutableStateOf(Recurrence.NONE.name) }

            CustomDropdownMenu(
                modifier = Modifier.padding(vertical = 14.dp),
                selectedOption = selectedRecurrence,
                onOptionSelected = {
                    selectedRecurrence = it
                }

            )

            // Notes
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                minLines = 5,
                maxLines = 5,
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text(text = "Notes") },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter), onClick = {}) {
            Text("Add Feed")
        }

    }

}

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val angle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Angle Animation"
    )

    Column(modifier = modifier) {
        // The "anchor" field
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            label = { Text("Recurrence") },
            trailingIcon = {
                Icon(
                    modifier = Modifier.rotate(degrees = angle),
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        )

        // The dropdown list (only shown when expanded)
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.LightGray)
                    .background(Color.White)
            ) {
                Recurrence.entries.forEach { option ->
                    Text(
                        text = option.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option.name)
                                expanded = false
                            }
                            .padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
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