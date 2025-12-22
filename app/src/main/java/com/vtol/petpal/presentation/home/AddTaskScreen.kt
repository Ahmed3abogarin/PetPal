package com.vtol.petpal.presentation.home

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.details.FoodDetails
import com.vtol.petpal.domain.model.tasks.details.MedDetails
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.model.tasks.details.WalkDetails
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.PetDropDownMenu
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.components.TextFieldVariant
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.home.components.TaskDatePicker
import com.vtol.petpal.presentation.home.components.TimePicker
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.convertDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

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

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { MediumTopAppBar(title = { Text("New Reminder") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState), // Make screen scrollable
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
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

            HorizontalDivider()

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
                        note = ""
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
fun AddTaskScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel,navigateUp: () -> Unit) {

    var typeIndex by remember { mutableIntStateOf(-1) }

    var recurrenceIndex by remember { mutableIntStateOf(-1) }

    var selectedType by remember { mutableStateOf<TaskType?>(null) }


    var recurrence by remember { mutableStateOf<RepeatInterval?>(RepeatInterval.Never) }

    var showDatePicker by remember { mutableStateOf(false) }

    var dueDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    var dueTime by remember { mutableStateOf(LocalTime.now()) }

    val scrollState = rememberScrollState()

    // Dynamic fields
    var food by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    var clinic by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }

    var duration by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var showNote by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }


    // button state
    var isSaving by remember { mutableStateOf(false) }

    val isValid by remember {
        derivedStateOf {
            val staticFieldsValid = selectedType != null

            // Dynamic fields validation
            val dynamicFieldsValid = when (selectedType){
                TaskType.FOOD -> food.isNotBlank() && amount.isNotBlank()
                TaskType.VET -> clinic.isNotBlank() && reason.isNotBlank()
                TaskType.MEDICATION -> medicineName.isNotBlank() && dosage.isNotBlank()
                TaskType.WALK -> duration.isNotBlank() && location.isNotBlank()
                else -> false
            }
            staticFieldsValid && dynamicFieldsValid
        }

    }





    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
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
                    .align(Alignment.TopCenter)
                    .verticalScroll(scrollState)
            ) {
                Text(text = "For which pet?")
                Spacer(modifier = Modifier.height(16.dp))
                PetDropDownMenu(
                    petsList = listOf(Pet(petName = "Blind Pew"), Pet(petName = "White lady")),
                    onConfirm = {
                        Log.v("Pets", "List size is${it.size}")
                        it.forEachIndexed { index, pet ->
                            Log.v("Pets", "index: $index \n pet: $pet")
                        }

                    },
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Select task type")
                MyDropDownMenu(
                    items = TaskType.entries.map { it },
                    selectedIndex = typeIndex,
                    onItemSelected = { index, type ->
                        // clear the focus
                        typeIndex = index
                        selectedType = type

                    },
                    label = "Task Type"
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
                        onDismiss = {
                            showDatePicker = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time picker
                TimePicker(onTimeChanged = {
                    dueTime = it
                })
                Spacer(modifier = Modifier.height(16.dp))

                // Recurrence
//                Text(text = "Recurrence")
//                Spacer(modifier = Modifier.height(16.dp))

                MyDropDownMenu(
                    expendedIcon = Icons.Default.Repeat,
                    closedIcon = Icons.Default.Repeat,
                    variant = TextFieldVariant.Filled,
                    items = RepeatInterval.entries.map { it },
                    selectedIndex = recurrenceIndex,
                    onItemSelected = { index, type ->
                        // clear the focus
                        recurrenceIndex = index
                        recurrence = type
                    },
                    label = "Recurrence"
                )


                // TODO : don't display the associated fields until the user specify the task type

                // ------------------------ The Dynamic Fields -------------------------------
                selectedType?.let {
                    // display only the associated fields with the task type

                    // Food fields
                    AnimatedVisibility(visible = selectedType == TaskType.FOOD) {
                        Column {
                            AppTextField(
                                value = food,
                                placeHolder = "Food",
                                onValueChanged = { food = it }
                            )
                            AppTextField(
                                value = amount,
                                placeHolder = "Amount",
                                onValueChanged = { amount = it }
                            )
                        }
                    }


                    // VET fields

                    AnimatedVisibility(visible = selectedType == TaskType.VET) {
                        Column {
                            Text(text = "Clinic details")
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTextField(
                                value = clinic,
                                placeHolder = "Clinic name",
                                onValueChanged = { clinic = it }
                            )
                            AppTextField(
                                value = reason,
                                placeHolder = "Why are you visiting the vet?",
                                onValueChanged = { reason = it }
                            )
                        }
                    }

                    // MED fields

                    AnimatedVisibility(visible = selectedType == TaskType.MEDICATION) {
                        Column {
                            Text(text = "Medication Details")
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTextField(
                                value = medicineName,
                                placeHolder = "Medicine Name (e.g. Heartgard)",
                                onValueChanged = { medicineName = it }
                            )
                            AppTextField(
                                value = dosage,
                                placeHolder = "Dosage (e.g. 1 pill)",
                                onValueChanged = { dosage = it }
                            )
                        }
                    }

                    // Walk fields

                    AnimatedVisibility(visible = selectedType == TaskType.WALK) {
                        Column {
                            Text(text = "Walk Details")
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = duration,
                                onValueChange = { newValue ->
                                    // Allow digits only
                                    if (newValue.all { it.isDigit() }) {
                                        duration = newValue
                                    }
                                },
                                colors = TextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp),
                                label = { Text("Duration") },
                                placeholder = { Text("30") },
                                suffix = { Text("min") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                modifier = modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTextField(
                                value = location,
                                placeHolder = "Location (e.g. Park)",
                                onValueChanged = { location = it }
                            )
                        }
                    }

                    if (showNote) {

                        AppTextField(
                            value = note,
                            minLines = 4,
                            onValueChanged = { note = it },
                            placeHolder = "Add a note"
                        )

                    } else {
                        val coroutine = rememberCoroutineScope()
                        Row(
                            modifier = Modifier.clickable {
                                showNote = true
                                coroutine.launch {
                                    kotlinx.coroutines.yield()
                                    scrollState.animateScrollTo(value = scrollState.maxValue)
                                }
                            },
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MainPurple
                            )
                            Text(
                                text = "Add note",
                                color = MainPurple,
                                fontWeight = FontWeight.Medium
                            )

                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Spacer(modifier = Modifier.height(42.dp))
                }
            }

            // the button should be static in the bottom
            SaveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "Save",
                isLoading = isSaving,
                enabled = isValid && !isSaving,
                color = MainPurple
            ) {
                isSaving = true
                val gson = Gson()
                val jsonDetails = when (selectedType) {
                    TaskType.FOOD -> gson.toJson(FoodDetails(food, amount))
                    TaskType.VET -> gson.toJson(VetDetails(clinic, reason))
                    TaskType.MEDICATION -> gson.toJson(MedDetails(medicineName, dosage))
                    TaskType.WALK -> gson.toJson(WalkDetails(duration.toIntOrNull() ?: 0, location))
                    else -> ""
                }

                // 2. Create the combined DateTime (LocalDate + LocalTime)
                // Convert your dueDate and dueTime into a single Long timestamp
                val combinedDateTime = LocalDateTime.of(dueDate, dueTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                // 3. Construct the Final Task Object
                val newTask = Task(
                    petId = 1, // You'll eventually get this from your PetDropDown selection
                    title = selectedType?.name ?: "Task",
                    type = selectedType ?: TaskType.FOOD,
                    dateTime = combinedDateTime,
                    details = jsonDetails,
                    note = note,
                    isCompleted = false
                )

                // 4- call the vm TODO
                viewModel.insertTask(newTask)


                // 5- navigate back after successfully save it
                navigateUp()

            }
        }
    }
}


@Preview
@Composable
fun AddTaskScreenPreview() {
    PetPalTheme {


    }
}