package com.vtol.petpal.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.presentation.components.PetDropDownMenu
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.home.components.TaskDatePicker
import com.vtol.petpal.presentation.home.components.TimePicker
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.convertDate
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen2(
    // Callback to pass data back to your ViewModel
    onSaveTask: (Task) -> Unit,
) {
    // === 1. Common State ===
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TaskType.FOOD) }

    // === 2. Dynamic State (One var for every possible field) ===
    // Food Fields
    var foodBrand by remember { mutableStateOf("") }
    var foodAmount by remember { mutableStateOf("") }

    // Vet Fields
    var doctorName by remember { mutableStateOf("") }
    var clinicAddress by remember { mutableStateOf("") }

    // Med Fields
    var medDosage by remember { mutableStateOf("") }

    Scaffold(
        topBar = { MediumTopAppBar(title = { Text("New Reminder") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make screen scrollable
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- SECTION A: The Basics ---
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title (e.g. Morning Walk)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("What type of task is this?", style = MaterialTheme.typography.titleMedium)

            // A simple Row of Radio Buttons to select type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TaskType.entries.forEach { type ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(
                            selected = (type == selectedType),
                            onClick = { selectedType = type }
                        )
                        Text(
                            text = type.name.lowercase(), // Simple formatting
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Divider()

            // --- SECTION B: The Dynamic Fields ---

            // 1. FOOD FIELDS
            AnimatedVisibility(visible = selectedType == TaskType.FOOD) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Food Details",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = foodBrand,
                        onValueChange = { foodBrand = it },
                        label = { Text("Brand Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = foodAmount,
                        onValueChange = { foodAmount = it },
                        label = { Text("Amount (grams/cups)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 2. VET FIELDS
            AnimatedVisibility(visible = selectedType == TaskType.VET) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Clinic Details",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = doctorName,
                        onValueChange = { doctorName = it },
                        label = { Text("Doctor's Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = clinicAddress,
                        onValueChange = { clinicAddress = it },
                        label = { Text("Clinic Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. MED FIELDS
            AnimatedVisibility(visible = selectedType == TaskType.MEDICATION) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Medication Details",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = medDosage,
                        onValueChange = { medDosage = it },
                        label = { Text("Dosage (e.g. 1 pill)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- SECTION C: The Save Button ---
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    // 1. Create the specific object based on type
//                    val jsonDetails = when(selectedType) {
//                        TaskType.FOOD -> Gson().toJson(FoodDetails(foodBrand, foodAmount.toIntOrNull() ?: 0))
//                        TaskType.VET -> Gson().toJson(VetDetails(doctorName, clinicAddress))
//                        TaskType.MEDICATION -> Gson().toJson(MedDetails(medDosage, "See label"))
//                    }

                    // 2. Create the Task Entity
                    val newTask = Task(
                        petId = 123, // Replace with real Pet ID
                        title = title,
                        type = selectedType,
                        dateTime = System.currentTimeMillis(), // Replace with DatePicker value
                        details = "jsonDetails",
                        description = ""
                    )

                    // 3. Send it up!
                    onSaveTask(newTask)
                }
            ) {
                Text("Save Reminder")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    var taskType by remember { mutableStateOf<TaskType?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }

    var dueDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    var dueTime by remember { mutableStateOf(LocalTime.now()) }


    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title = { Text("New Reminder") }
            )
        }

    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "For which pet?")
                Spacer(modifier = Modifier.height(16.dp))
                PetDropDownMenu(
                    petsList = listOf(Pet(petName = "Blind Pew"), Pet(petName = "White lady")),
                    onConfirm = {},
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Select task type")
                MyDropDownMenu(
                    items = TaskType.entries.map { it },
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, type ->
                        // clear the focus

                        selectedIndex = index
                        taskType = type

                    },
                    label = "e.g Food"
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Date & time")
                Spacer(modifier = Modifier.height(16.dp))


                // Date picker dialog
                TextField(
                    value = dueDate.convertDate(),
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {},
                    label = { Text("Due date") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
//                        focusManger.clearFocus()
                            showDatePicker = true
                        },
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

                if (showDatePicker) {
                    TaskDatePicker(
                        onDateSelected = { date ->
                            dueDate = date
                        },
                        onDismiss = {}
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time picker
                TimePicker(onTimeChanged = {
                    dueTime = it
                })

                // TODO : don't display the associated fields until the user specify the task type
                taskType?.let {
                    // display only the associated fields with the task type

                }
            }

            // the button should be static in the bottom
            SaveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "Save",
                color = MainPurple
            ) {

            }
        }
    }
}

@Preview
@Composable
fun AddTaskScreenPreview() {
    PetPalTheme {
        AddTaskScreen()

    }
}