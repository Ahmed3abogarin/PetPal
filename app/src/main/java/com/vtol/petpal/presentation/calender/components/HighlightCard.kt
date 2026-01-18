package com.vtol.petpal.presentation.calender.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.convertDate
import com.vtol.petpal.util.toTimeString
import java.time.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun HighlightCard(modifier: Modifier = Modifier, tasks: List<Task>?, date: LocalDate, petMap: Map<String, String>) {

    val groupedTasks = tasks?.groupBy { it.title }


    Card(
        modifier = modifier.fillMaxWidth().heightIn(min = 200.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            // Left content (the pet icon)
            Surface(modifier = Modifier.size(82.dp), shape = CircleShape) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    painter = painterResource(R.drawable.pet_placeholder),
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.width(16.dp))


            // Right content
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = date.convertDate(), style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(10.dp))

                when {
                    groupedTasks == null -> {
                        Box(modifier = Modifier
                            .padding(start = 10.dp), contentAlignment = Alignment.Center){

                            Text(text = "No tasks")
                        }

                    }
                    else -> {

                        Column {

                            // Day's tasks list

                            groupedTasks.forEach { (taskTitle, taskList) ->

                                Row {
                                    Image(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .padding(8.dp),
                                        painter = painterResource(R.drawable.pet_placeholder),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {

                                        val petNames = taskList.map { petMap[it.petId] ?: "Unknown" }

                                        // Pets names
                                        Text(
                                            text = petNames.joinToString(", "),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = taskTitle, style = MaterialTheme.typography.labelMedium)
                                    }
                                }



                            }



                            Spacer(modifier = Modifier.height(16.dp))

                            val earliestTime = tasks.minOfOrNull { it.dateTime }?.toTimeString() ?: ""
                            if (earliestTime.isNotEmpty()) {
                                Text(text = earliestTime)
                            }
                        }




                    }
                }

            }
        }
    }
}
