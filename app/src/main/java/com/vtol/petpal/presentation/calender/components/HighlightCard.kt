package com.vtol.petpal.presentation.calender.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.presentation.components.PetTaskCard
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.TextPurple
import com.vtol.petpal.util.convertDate
import kotlinx.coroutines.delay
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightCard(
    tasks: List<TaskUi>?,
    date: LocalDate,
    petMap: Map<String, String>,
    showToast: () -> Unit,
    onDeleteAll: (String) -> Unit,
    onDeleteThis: (TaskUi) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ExtraLightPurple)
                        .padding(10.dp)
                        .size(18.dp),
                    painter = painterResource(R.drawable.ic_calendar_outlined),
                    contentDescription = "",
                    tint = MainPurple
                )

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = date.convertDate(),
                    color = TextPurple,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 1.dp, color = LightPurple, shape = CircleShape)
                    .clickable {
                        if (tasks.isNullOrEmpty()) {
                            showToast()
                            return@clickable
                        }
                        showDialog = true
                    }
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "See day",
                    fontSize = 10.sp,
                    color = TextPurple
                )
                Icon(
                    modifier = Modifier
                        .size(8.dp)
                        .rotate(180f),
                    painter = painterResource(R.drawable.ic_arrow),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        when {
            tasks.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No tasks for this day", color = LightPurple)
                }
            }

            else -> {
                var isVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(50)
                    isVisible = true
                }
                AnimatedVisibility(
                    visible = isVisible
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Day's tasks list
                        tasks.take(3).forEach { task ->
                            val petName = petMap[task.petId] ?: "Unknown"
                            PetTaskCard(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                task = task, petName = petName,
                                onDeleteThis = { onDeleteThis(task) },
                                onDeleteAll = { onDeleteAll(task.id) }
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }
    }

    if (showDialog && !tasks.isNullOrEmpty()) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            containerColor = BackgroundColor,
            onDismissRequest = {
                showDialog = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                        text = date.convertDate(),
                        color = TextPurple
                    )
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { showDialog = false },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        val petName = petMap[task.petId] ?: "Unknown"
                        PetTaskCard(
                            task = task,
                            petName = petName,
                            onDeleteThis = {},
                            onDeleteAll = {}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighlighPrevew() {
    PetPalTheme {
        HighlightCard(
            tasks = listOf(
                TaskUi(
                    petId = "1",
                    title = "Task 1",
                    note = "Note 1",
                    type = TaskType.VET,
                    dateTime = System.currentTimeMillis(),
                    isCompleted = false,
                    repeatInterval = null,
                    details = VetDetails("Clinic 1", "Just checking in"),
                    id = "4",
                    syncStatus = SyncStatus.SYNCED
                )
            ),
            date = LocalDate.now(),
            petMap = mapOf(),
            showToast = {},
            onDeleteAll = {},
            onDeleteThis = {}
        )
    }
}