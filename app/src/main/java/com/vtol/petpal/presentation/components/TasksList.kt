package com.vtol.petpal.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun TasksList(
    modifier: Modifier = Modifier,
    isToday: Boolean,
    tasksList: List<TaskUi>,
    petMap: Map<String, String>,
    onToggleClicked: (String, Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.padding(horizontal = 16.dp),
        border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
    ) {
        tasksList.forEachIndexed { index, task ->
            TaskCard(
                isToday = isToday,
                task = task,
                petName = petMap[task.petId] ?: "Unknown",
                onCheckedChange = {
                    onToggleClicked(task.id, it)
                }
            )
            if (index != tasksList.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MainPurple.copy(0.3f),
                    thickness = 0.2.dp
                )
            }
        }
    }
}