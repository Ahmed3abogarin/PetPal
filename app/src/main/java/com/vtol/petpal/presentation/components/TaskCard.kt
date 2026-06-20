package com.vtol.petpal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.presentation.pets.components.FlippingText
import com.vtol.petpal.ui.theme.LightOrange
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.Orange
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Pink100
import com.vtol.petpal.ui.theme.Pink50
import com.vtol.petpal.ui.theme.SubtitleGray
import com.vtol.petpal.util.getPetTaskTitle
import com.vtol.petpal.util.toFriendlyDate
import com.vtol.petpal.util.toTimeString

@Composable
fun TaskCard(
    task: TaskUi,
    petName: String? = null,
    isToday: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val taskImg = when (task.type) {
        TaskType.VET -> R.drawable.ic_vets
        TaskType.FEED -> R.drawable.ic_feed
        TaskType.MEDICATION -> R.drawable.ic_pharmacy
        TaskType.WALK -> R.drawable.ic_parks
    }

    val (iconColor, bgColor) = when (task.type) {
        TaskType.FEED -> Pair(Color(0xFF0ECF66), Color(0xFFE6F5E8))
        TaskType.MEDICATION -> Pair(Pink100, Pink50)
        TaskType.WALK -> Pair(Orange, LightOrange) //Pair(MainPurple, CellsBgPurple)
        TaskType.VET -> Pair(Color.Red, Color.Red.copy(alpha = 0.2f))
    }

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = bgColor,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(14.dp),
                    painter = painterResource(taskImg),
                    tint = iconColor,
                    contentDescription = "Pet Image"
                )
            }


            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                petName?.let {
                    val (title, subTitle) = getPetTaskTitle(task, petName)
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    val dateTimeTxt =
                        "${task.dateTime.toFriendlyDate()} • ${task.dateTime.toTimeString()}"
                    val list = listOf(dateTimeTxt, subTitle)

                    FlippingText(Modifier,list, textColor = SubtitleGray, fontWeight = FontWeight.Normal)

                }
            }
        }


        if (isToday){
            Box(modifier = Modifier.size(24.dp)) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onCheckedChange(!task.isCompleted) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MainPurple
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTaskCard() {
    PetPalTheme {
        TaskCard(
            petName = "Buddy",
            task = TaskUi(
                id = "task_5",
                petId = "pet_1",
                title = "Vet Checkup",
                note = "Annual vaccination appointment",
                type = TaskType.FEED,
                dateTime = System.currentTimeMillis() + 604800000,
                isCompleted = true,
                repeatInterval = null,
                details = null,
                syncStatus = SyncStatus.SYNCED
            ),
            isToday = false
        ) { }
    }
}
