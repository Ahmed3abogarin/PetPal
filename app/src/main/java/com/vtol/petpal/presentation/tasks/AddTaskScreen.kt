package com.vtol.petpal.presentation.tasks

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.components.PetDropDownMenu
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.home.components.PermissionRationaleDialog
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.presentation.tasks.components.DateTimeButtons
import com.vtol.petpal.presentation.tasks.components.RepeatCard
import com.vtol.petpal.presentation.tasks.components.TaskTypeCard

@Composable
fun AddTaskScreen(
    state: AddTaskState,
    event: (AddTaskUserIntent) -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(petPalGradient)
                .statusBarsPadding()
                .padding(top = 20.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppIconButton(modifier = Modifier.padding(start = 16.dp)) { navigateUp() }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Add Task",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "Schedule a pet care activity",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }


        Column {
            Column(
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 16.dp)
                    .animateContentSize()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp), text = "FOR WHICH PET? *",
                    fontSize = 14.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )

                PetDropDownMenu(
                    isLoading = state.isPetsLoading,
                    petsList = state.pets,
                    selectedPet = state.selectedPet,
                    onConfirm = {
                        event(AddTaskUserIntent.PetSelected(it))
                    }
                )


                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(bottom = 8.dp), text = "TASK TYPE *",
                    fontSize = 14.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TaskType.entries.forEach { task ->
                        TaskTypeCard(
                            isSelected = state.selectedType == task,
                            task = task.txt,
                            icon = task.icon
                        ) {
                            event(AddTaskUserIntent.TypeSelected(task))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(bottom = 8.dp), text = "When? *",
                    fontSize = 14.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )

                DateTimeButtons(
                    selectedDate = state.dueDate,
                    selectedTime = state.dueTime,
                    onDateChanged = {
                        event(AddTaskUserIntent.DateChanged(it))
                    },
                    onTimeChanged = {
                        event(AddTaskUserIntent.TimeChanged(it))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(bottom = 8.dp), text = "REPEAT",
                    fontSize = 14.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    RepeatInterval.entries.forEach { repeat ->

                        RepeatCard(txt = repeat.name, isSelected = state.recurrence == repeat) {
                            event(AddTaskUserIntent.RecurrenceChanged(repeat))
                        }
                    }
                }


                // Dynamic fields
                state.selectedType?.let { type ->
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = type == TaskType.FEED) {
                        Column {

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp), text = "FOOD",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_location,
                                placeHolder = "e.g. Dry kibble, wet food, raw diet…",
                                value = state.clinic,
                                onValueChanged = { event(AddTaskUserIntent.ClinicChanged(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "Amount",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                placeHolder = "e.g. 1 cup, half a can, 200g…",
                                value = state.reason,
                                minLines = 2,
                                onValueChanged = { event(AddTaskUserIntent.ReasonChanged(it)) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "NOTE (OPTIONAL)",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_edit,
                                placeHolder = "e.g. Mix with warm water…",
                                value = state.note,
                                onValueChanged = { event(AddTaskUserIntent.NoteChanged(it)) }
                            )
                        }
                    }

                    AnimatedVisibility(visible = type == TaskType.VET) {
                        Column {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp), text = "CLINIC NAME",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_location,
                                placeHolder = "e.g. Sunrise Animal Clinic…",
                                value = state.clinic,
                                onValueChanged = { event(AddTaskUserIntent.ClinicChanged(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "REASON FOR VISIT (OPTIONAL)",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                placeHolder = "e.g. Annual checkup, limping, vaccination…",
                                value = state.reason,
                                minLines = 2,
                                onValueChanged = { event(AddTaskUserIntent.ReasonChanged(it)) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "NOTE (OPTIONAL)",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_edit,
                                placeHolder = "e.g. Bring past records…",
                                value = state.note,
                                onValueChanged = { event(AddTaskUserIntent.NoteChanged(it)) }
                            )
                        }
                    }

                    AnimatedVisibility(visible = type == TaskType.MEDICATION) {

                        Column {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp), text = "MEDICINE NAME",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_location,
                                placeHolder = "e.g. Heartgard, Apoquel, Metacam…",
                                value = state.clinic,
                                onValueChanged = { event(AddTaskUserIntent.ClinicChanged(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "DOSAGE",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                placeHolder = "e.g. 1 tablet, 0.5 ml, 25 mg…",
                                value = state.reason,
                                minLines = 2,
                                onValueChanged = { event(AddTaskUserIntent.ReasonChanged(it)) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "NOTE (OPTIONAL)",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_edit,
                                placeHolder = "e.g. Give after meals, avoid sunlight…",
                                value = state.note,
                                onValueChanged = { event(AddTaskUserIntent.NoteChanged(it)) }
                            )
                        }
                    }

                    AnimatedVisibility(visible = type == TaskType.WALK) {
                        Column {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp), text = "ROUTE / LOCATION",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_location,
                                placeHolder = "e.g. Heartgard",
                                value = state.clinic,
                                onValueChanged = { event(AddTaskUserIntent.ClinicChanged(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "DURATION",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                placeHolder = "e.g. 20 min, 1 hour…",
                                value = state.reason,
                                minLines = 2,
                                onValueChanged = { event(AddTaskUserIntent.ReasonChanged(it)) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "NOTE (OPTIONAL)",
                                fontSize = 14.sp,
                                color = LightPurple,
                                fontWeight = FontWeight.Medium
                            )
                            PetTextField(
                                leadingIcon = R.drawable.ic_edit,
                                placeHolder = "e.g. Avoid the road by the school…",
                                value = state.note,
                                onValueChanged = { event(AddTaskUserIntent.NoteChanged(it)) }
                            )
                        }
                    }
                }
            }

            SaveButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                icon = Icons.Default.Save,
                text = "Save task",
                enabled = state.selectedType != null,
                color = MainPurple
            ) {
                event(AddTaskUserIntent.SaveClicked)
            }

            val exactAlarmSettingsLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                event(AddTaskUserIntent.ExactAlarmPermissionResult)
            }

            if (state.showExactAlarmDialog) {
                PermissionRationaleDialog(
                    title = "Enable Notifications",
                    message = "Allow PetPal to send reminders so you never miss a pet task.",
                    onConfirm = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            exactAlarmSettingsLauncher.launch(
                                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                            )
                        }
                    },
                    onDismiss = { event(AddTaskUserIntent.DismissExactAlarmDialog) }
                )
            }

            if (state.showNotificationDialog) {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    event(AddTaskUserIntent.NotificationPermissionResult(granted))
                }
                PermissionRationaleDialog(
                    title = "Enable Notifications",
                    message = "Allow reminders so you don't miss pet tasks.",
                    onConfirm = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                    onDismiss = { event(AddTaskUserIntent.DismissNotificationDialog) }
                )
            }
        }
    }
}


@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        AddTaskScreen(
            state = AddTaskState(),
            event = {}
        ) {}
    }
}