package com.vtol.petpal.presentation.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.vtol.petpal.presentation.components.filledTextFieldColors
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.home.components.PermissionRationaleDialog
import com.vtol.petpal.presentation.home.components.TaskDatePicker
import com.vtol.petpal.presentation.home.components.TimePicker
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.util.convertDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    petsList: List<Pet>,
    viewModel: HomeViewModel,
    navigateUp: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    var typeIndex by remember { mutableIntStateOf(-1) }

    var recurrenceIndex by remember { mutableIntStateOf(-1) }

    var selectedType by remember { mutableStateOf<TaskType?>(null) }

    var selectedPet by remember { mutableStateOf(if (petsList.isNotEmpty()) petsList.first() else Pet()) }

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
            val dynamicFieldsValid = when (selectedType) {
                TaskType.FEED -> food.isNotBlank() && amount.isNotBlank()
                TaskType.VET -> clinic.isNotBlank() && reason.isNotBlank()
                TaskType.MEDICATION -> medicineName.isNotBlank() && dosage.isNotBlank()
                TaskType.WALK -> duration.isNotBlank() && location.isNotBlank()
                else -> false
            }
            staticFieldsValid && dynamicFieldsValid
        }

    }

    LaunchedEffect(state.taskSaved) {
        if (state.taskSaved) {
            viewModel.resetTaskSaved()
            navigateUp()
        }
    }
// POST_NOTIFICATIONS launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onNotificationPermissionResult(granted)
    }

    // SCHEDULE_EXACT_ALARM launcher
    val exactAlarmSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.onExactAlarmPermissionResult()
    }

    // POST_NOTIFICATIONS dialog
    if (state.showNotificationPermissionDialog) {
        PermissionRationaleDialog(
            title = "Enable Notifications",
            message = "Allow PetPal to send reminders so you never miss a pet task.",
            onConfirm = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            },
            onDismiss = { viewModel.onNotificationPermissionDismissed() }
        )
    }

    // SCHEDULE_EXACT_ALARM dialog
    if (state.showExactAlarmPermissionDialog) {
        PermissionRationaleDialog(
            title = "Allow Exact Reminders",
            message = "To remind you at the exact time, PetPal needs permission to schedule precise alarms. You'll be taken to Settings briefly.",
            confirmText = "Open Settings",
            onConfirm = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    exactAlarmSettingsLauncher.launch(
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    )
                }
            },
            onDismiss = { viewModel.onPermissionDismissed() }
        )
    }



    Column(
        modifier = Modifier.background(BackgroundColor)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .verticalScroll(scrollState)
    ) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                modifier = Modifier.clickable { navigateUp() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = ""
            )


            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "New Reminder", style = MaterialTheme.typography.titleLarge)

        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(text = "For which pet?")
        Spacer(modifier = Modifier.height(16.dp))

        PetDropDownMenu(
            petsList = petsList,
            selectedPet = selectedPet,
            onConfirm = {
                selectedPet = it
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
            AnimatedVisibility(visible = selectedType == TaskType.FEED) {
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
                        colors = filledTextFieldColors(),
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

        Spacer(modifier = Modifier.weight(1f))


        // the button should be static in the bottom
        SaveButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Save",
            isLoading = isSaving,
            enabled = isValid && !isSaving,
            color = MainPurple
        ) {
            isSaving = true
            val gson = Gson()
            val jsonDetails = when (selectedType) {
                TaskType.FEED -> gson.toJson(FoodDetails(food, amount))
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
                petId = selectedPet.id, // You'll eventually get this from your PetDropDown selection
                title = selectedType?.name ?: "Task",
                type = selectedType ?: TaskType.FEED,
                dateTime = combinedDateTime,
                details = jsonDetails,
                repeatInterval = recurrence,
                note = note,
                isCompleted = false
            )

            // 4- call the vm TODO
            viewModel.insertTask(newTask)
        }
    }
}