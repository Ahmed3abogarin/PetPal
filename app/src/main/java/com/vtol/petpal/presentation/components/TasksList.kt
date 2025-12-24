package com.vtol.petpal.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.presentation.home.HomeState
import com.vtol.petpal.presentation.home.HomeViewModel
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun TasksList2(modifier: Modifier = Modifier, state: HomeState) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_walk),
                    contentDescription = null
                )
                Text(text = "Walk", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null
                )
                Text(text = "07:00AM", fontSize = 18.sp, modifier = Modifier.padding(start = 6.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_fed),
                    contentDescription = null
                )
                Text(text = "Feed", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null
                )
                Text(text = "04:00PM", fontSize = 18.sp, modifier = Modifier.padding(start = 6.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_pill),
                    contentDescription = null
                )
                Text(text = "Medicine", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null
                )
                Text(text = "06:00PM", fontSize = 18.sp, modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}


@Composable
fun TasksList(modifier: Modifier = Modifier, items: List<Task> = emptyList()) {

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) {
            TaskCard(it)
        }

    }


}

@Composable
fun TasksListTest(modifier: Modifier = Modifier) {

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(2) {
            val task = Task(
                5,
                22,
                "Blind Pew, Max, Lionel ",
                "6:00PM",
                type = TaskType.VET,
                dateTime = 55
            )
            TaskCard(task)
        }


    }


}

@Composable
fun TaskCard(task: Task) {

    val (taskType, taskImg) = when (task.type) {
        TaskType.VET -> "Vet" to R.drawable.ic_vet
        TaskType.FEED -> "Feed" to R.drawable.ic_fed
        TaskType.MEDICATION -> "Medicine" to R.drawable.ic_pill
        TaskType.WALK -> "Walk" to R.drawable.ic_walk
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Image(
                modifier = Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(Color(0x8BDCC5FF))
                    .padding(12.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(taskImg), contentDescription = "Pet Image"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = taskType,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "6:00PM",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.width(1.dp))

                    }
                }

                Text(
                    text = "Blind Pew, Max, Lionel",
                )
            }
            RadioButton(selected = false, onClick = { /*TODO*/ })

        }
    }

}

@Preview
@Composable
fun MyPreview() {
    /*
     Displays:
     - Pet/s images
     - Task Type
     - Pet/s name/s
     - Time
     - RadioButton -
     */
    PetPalTheme {
        TaskCard(
            Task(
                5,
                22,
                "Blind Pew, Max, Lionel ",
                "6:00PM",
                type = TaskType.VET,
                dateTime = 55
            )
        )

    }
}