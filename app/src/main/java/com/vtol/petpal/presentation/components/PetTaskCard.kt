package com.vtol.petpal.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.presentation.calender.components.DeleteTaskDialog
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.LightOrange
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.Orange
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Pink100
import com.vtol.petpal.ui.theme.Pink50
import com.vtol.petpal.util.formatDate
import com.vtol.petpal.util.getPetTaskTitle
import com.vtol.petpal.util.toTimeString
import com.vtol.petpal.util.truncate

@Composable
fun PetTaskCard(
    modifier: Modifier = Modifier,
    task: TaskUi,
    petName: String,
    bgColor: Color = Color.White,
    showDate: Boolean = false,
    showMore: Boolean = true,
    onDeleteThis: () -> Unit,
    onDeleteAll: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val (mainColor, secColor) = when (task.type) {
        TaskType.FEED -> Pair(Orange, LightOrange)
        TaskType.MEDICATION -> Pair(Color.Red, Color.Red.copy(alpha = 0.2f))
        TaskType.WALK -> Pair(MainPurple, CellsBgPurple)
        TaskType.VET -> Pair(Pink100, Pink50)
    }

    val icon = when (task.type) {
        TaskType.FEED -> R.drawable.ic_task_feed
        TaskType.MEDICATION -> R.drawable.ic_task_meds
        TaskType.WALK -> R.drawable.ic_task_walk
        TaskType.VET -> R.drawable.ic_task_vet

    }

    val (title, subTitle) = getPetTaskTitle(task,petName)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                0.3.dp,
                color = LightPurple,
                shape = RoundedCornerShape(14.dp)
            )
            .background(bgColor)
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
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title.truncate(24),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                val bodyTxt = if (showDate) task.dateTime.formatDate() else subTitle
                Text(
                    text = bodyTxt.truncate(25),
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
            if (showMore) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    Icon(
                        modifier = Modifier
                            .clickable { expanded = true }
                            .size(20.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                showDeleteDialog = true
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        )
                    }
                }

            }
        }
    }

    if (showDeleteDialog) {
        DeleteTaskDialog(
            isRecurring = true,
            onDismiss = { showDeleteDialog = false },
            onDeleteAll = onDeleteAll,
            onDeleteThis = onDeleteThis
        )
    }
}

@Preview
@Composable
fun MyPreviewPet() {
    PetPalTheme {
        PetTaskCard(
            task = TaskUi(
                petId = "1",
                title = "Task 1",
                note = "Note 1",
                type = TaskType.VET,
                dateTime = System.currentTimeMillis(),
                isCompleted = false,
                repeatInterval = null,
                id = "",
                details = VetDetails("Clinic 1", "Just checking in"),
                syncStatus = SyncStatus.SYNCED
            ),
            petName = "gsdg",
            onDeleteAll = {},
            onDeleteThis = {}
        )

    }
}