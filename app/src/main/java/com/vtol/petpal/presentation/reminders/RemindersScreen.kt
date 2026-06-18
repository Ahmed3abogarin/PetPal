package com.vtol.petpal.presentation.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.presentation.reminders.components.ReminderSwipeableRow
import com.vtol.petpal.presentation.reminders.components.SectionHeader
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    modifier: Modifier = Modifier,
    overdueTasks: List<TaskUi>,
    upcomingTasks: List<TaskUi>,
    onToggleCompletion: (String, Boolean) -> Unit,
    onDeleteTask: (String) -> Unit,
    navigateUp: () -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {

            Box(
                modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .padding(horizontal = 6.dp)
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                    onClick = navigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "back arrow"
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Reminders",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier= Modifier.height(18.dp))
        }


        // --- OVERDUE SECTION ---
        if (overdueTasks.isNotEmpty()) {
            item {
                SectionHeader(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Overdue Tasks",
                    count = overdueTasks.size,
                    color = MaterialTheme.colorScheme.error
                )
            }
            items(overdueTasks, key = { it.id }) { task ->
                ReminderSwipeableRow(
                    task = task,
                    isOverdue = true,
                    onToggleCompletion = onToggleCompletion,
                    onDeleteTask = onDeleteTask,
                    modifier = Modifier.padding(horizontal = 16.dp).animateItem()
                )
            }
        }

        // --- UPCOMING SECTION ---
        if (upcomingTasks.isNotEmpty()) {
            item {
                SectionHeader(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Next 24 Hours",
                    count = upcomingTasks.size,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            items(upcomingTasks, key = { it.id }) { task ->
                ReminderSwipeableRow(
                    task = task,
                    isOverdue = false,
                    onToggleCompletion = onToggleCompletion,
                    onDeleteTask = onDeleteTask,
                    modifier = Modifier.padding(horizontal = 16.dp).animateItem()
                )
            }
        }

        // --- EMPTY STATE ---
        if (overdueTasks.isEmpty() && upcomingTasks.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = "All caught up! No pending reminders.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun RemindersPreview() {
    val now = System.currentTimeMillis()

    val allMockTasks = MockTaskData.dummyTasks
    val overdue = allMockTasks.filter { !it.isCompleted && it.dateTime < now }
    val upcoming =
        allMockTasks.filter { !it.isCompleted && it.dateTime >= now && it.dateTime <= (now + 24 * 60 * 60 * 1000L) }
    PetPalTheme {
        RemindersScreen(
            overdueTasks = overdue,
            upcomingTasks = upcoming,
            onDeleteTask = {},
            onToggleCompletion = { _, _ -> }
        ){}
    }
}

object MockTaskData {

    private val now = System.currentTimeMillis()
    private const val ONE_HOUR = 60 * 60 * 1000L
    private const val ONE_DAY = 24 * 60 * 60 * 1000L

    val dummyTasks = listOf(
        // 1. Overdue Task (Created 3 hours ago, uncompleted)
        TaskUi(
            id = "task_001",
            petId = "pet_bella",
            title = "Morning Insulin Shot",
            note = "Give with breakfast. Check glucose if possible.",
            type = TaskType.MEDICATION,
            dateTime = now - (3 * ONE_HOUR),
            deletedDates = emptyList(),
            isCompleted = false,
            repeatInterval = RepeatInterval.Daily,
            details = VetDetails(clinicName = "2 Units", reason = ""),
            syncStatus = SyncStatus.SYNCED
        ),

        // 2. Overdue Task (From yesterday, uncompleted)
        TaskUi(
            id = "task_002",
            petId = "pet_max",
            title = "Weekly Ear Cleaning",
            note = "Use the antiseptic solution from the vet.",
            type = TaskType.FEED,
            dateTime = now - ONE_DAY,
            deletedDates = emptyList(),
            isCompleted = false,
            repeatInterval = RepeatInterval.Weekly,
            details = null,
            syncStatus = SyncStatus.PENDING
        ),

        // 3. Upcoming Task (Due in 20 minutes)
        TaskUi(
            id = "task_003",
            petId = "pet_bella",
            title = "Evening Kibble Feed",
            note = "1.5 cups of dry salmon mix.",
            type = TaskType.FEED,
            dateTime = now + (20 * 60 * 1000L), // 20 mins from now
            deletedDates = emptyList(),
            isCompleted = false,
            repeatInterval = RepeatInterval.Daily,
            details = VetDetails(clinicName = "2 Units", reason = ""),
            syncStatus = SyncStatus.SYNCED
        ),

        // 4. Upcoming Task (Due in 4 hours)
        TaskUi(
            id = "task_004",
            petId = "pet_max",
            title = "Night Park Walk",
            note = "Bring the long leash and extra waste bags.",
            type = TaskType.WALK,
            dateTime = now + (4 * ONE_HOUR),
            deletedDates = emptyList(),
            isCompleted = false,
            repeatInterval = RepeatInterval.Daily,
            details = null,
            syncStatus = SyncStatus.SYNCED
        ),

        // 5. Completed Task (Done today)
        TaskUi(
            id = "task_005",
            petId = "pet_bella",
            title = "Brush Fur",
            note = "Bella is shedding a lot this week.",
            type = TaskType.MEDICATION,
            dateTime = now - (6 * ONE_HOUR),
            deletedDates = emptyList(),
            isCompleted = true, // Already checked off
            repeatInterval = RepeatInterval.Never,
            details = null,
            syncStatus = SyncStatus.MODIFIED
        ),

        // 6. Future Task with Exceptions (Simulating skipped recurrence for today)
        TaskUi(
            id = "task_006",
            petId = "pet_max",
            title = "Vet Checkup Follow-up",
            note = "Call to confirm bloodworm results.",
            type = TaskType.MEDICATION,
            dateTime = now + (2 * ONE_DAY),
            deletedDates = listOf(LocalDate.now()), // Skipped today, active in 2 days
            isCompleted = false,
            repeatInterval = RepeatInterval.Monthly,
            details = null,
            syncStatus = SyncStatus.SYNCED
        )
    )
}