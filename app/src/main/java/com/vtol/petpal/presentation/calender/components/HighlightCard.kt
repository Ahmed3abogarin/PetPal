package com.vtol.petpal.presentation.calender.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.FoodDetails
import com.vtol.petpal.domain.model.tasks.details.MedDetails
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.model.tasks.details.WalkDetails
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightOrange
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.Orange
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Pink100
import com.vtol.petpal.ui.theme.Pink50
import com.vtol.petpal.ui.theme.TextPurple
import com.vtol.petpal.util.convertDate
import com.vtol.petpal.util.toTimeString
import com.vtol.petpal.util.truncate
import java.time.LocalDate

@Composable
fun HighlightCard(
    tasks: List<TaskUi>?,
    date: LocalDate,
    petMap: Map<String, String>,
    navigateToDayTasks: (LocalDate) -> Unit
) {
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
                // Left content (the pet icon)
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
                    .padding(horizontal = 10.dp, vertical = 2.dp)
                    .clickable { navigateToDayTasks(date) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Say day",
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
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No tasks for this day")
                }
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Day's tasks list
                    tasks.take(3).forEach { task ->

                        val (mainColor, secColor) = when (task.type) {
                            TaskType.FEED -> Pair(Orange, LightOrange)
                            TaskType.MEDICATION -> Pair(Color.Red, Color.Red.copy(alpha = 0.6f))
                            TaskType.WALK -> Pair(MainPurple, CellsBgPurple)
                            TaskType.VET -> Pair(Pink100, Pink50)
                        }


                        val petName = petMap[task.petId] ?: "Unknown"

                        val (title, subTitle) = when (task.type) {
                            TaskType.FEED -> {
                                val d = task.details as? FoodDetails
                                "Feed $petName" to (d?.let { "${it.amount} of ${it.brand}" } ?: "")
                            }

                            TaskType.MEDICATION -> {
                                val d = task.details as? MedDetails
                                "$petName's Medication" to (d?.let {
                                    if (it.medicineName.isBlank()) return@let ""
                                    "${it.medicineName} • ${it.dosage}"
                                } ?: "")
                            }

                            TaskType.WALK -> {
                                val d = task.details as? WalkDetails
                                "Walk with $petName" to (d?.let {
                                    "${it.durationMinutes} min • ${it.location}"
                                } ?: ""
                                        )
                            }

                            TaskType.VET -> {
                                val d = task.details as? VetDetails
                                "Vet Visit for $petName" to (d?.let { "${it.clinicName} • ${it.reason}" }
                                    ?: "")
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    0.3.dp,
                                    color = LightPurple,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .background(Color.White)
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.height(IntrinsicSize.Min)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .aspectRatio(1 / 1f)
                                        .fillMaxHeight()
                                        .background(secColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        tint = mainColor,
                                        painter = painterResource(R.drawable.ic_vets),
                                        contentDescription = null
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = title.truncate(24),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = subTitle.truncate(25),
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        tint = mainColor,
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = task.dateTime.toTimeString(),
                                        color = mainColor,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }
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
                    id = 4,
                    syncStatus = SyncStatus.SYNCED
                )
            ),
            date = LocalDate.now(),
            petMap = mapOf(),
            navigateToDayTasks = {}
        )
    }
}