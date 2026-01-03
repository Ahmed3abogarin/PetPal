package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.presentation.pets.DetailsState
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.toTimeString

@Composable
fun VetsList(state: DetailsState, weightList: List<WeightRecord>, onAddWeightClicked: () -> Unit) {
    val vetTasks = remember(state.tasks) {
        state.tasks.filter { it.type == TaskType.VET }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text(text = state.error)

            }

            vetTasks.isEmpty() -> {
                Text(text = "No vet tasks")

            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {


                    item {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Weight", style = MaterialTheme.typography.headlineMedium)
                            FilledIconButton(
                                onClick = {
                                    onAddWeightClicked()
                                }, shape = CircleShape
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                        Text(
                            text = "Last updated: 13 minutes ago",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        /* the weights represent the Y axis (vertical line)
                           the dates represent the X axis (horizontal line)
                         */

                        AppChart(records = weightList.sortedBy { it.timestamp }.take(7))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Vet Visits", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))



                    }



                    items(vetTasks) { task ->
                        VetItem(task, task.dateTime.toTimeString())


                    }
                }

            }


        }


    }

}

@Composable
fun VetItem(task: Task, date: String) {

    val vet = remember(task.details) {
        task.details?.let {
            Gson().fromJson(it, VetDetails::class.java)
        }
    } ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0XFFF6F6F6))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LightPurple)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_vet),
                    contentDescription = ""
                )

            }

            // then the clinc name and Reason
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vet.clinicName, style = MaterialTheme.typography.titleMedium)
                Text(text = vet.reason, maxLines = 1, color = Color.LightGray)

            }

            // then the 'visit' tag
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LightPurple)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "visit", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "visit date: $date", fontSize = 12.sp)
        }
    }

}