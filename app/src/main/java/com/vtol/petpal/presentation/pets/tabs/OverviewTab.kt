package com.vtol.petpal.presentation.pets.tabs

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.components.ChipButton
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.pets.DetailsState
import com.vtol.petpal.presentation.pets.components.PetInfoItem
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.SemiTransparentPurple
import com.vtol.petpal.util.formatDate

@Composable
fun OverviewTab(
    modifier: Modifier = Modifier,
    state: DetailsState,
    onCheckedChanged: (Int, Boolean) -> Unit,
    onAddTaskClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        state.pet?.let { pet ->
            Row {
                PetInfoItem(
                    modifier = Modifier.weight(1f),
                    title = "Breed",
                    subTitle = pet.breed ?: "Unknown"
                )
                Spacer(modifier = Modifier.width(16.dp))

                val weight =
                    if (state.lastWeight.isNotEmpty()) state.lastWeight.last().weight.toString() else "Unknown"
                PetInfoItem(modifier = Modifier.weight(1f), title = "Weight", subTitle = weight)
            }
            Spacer(modifier = modifier.height(16.dp))

            Row {
                PetInfoItem(
                    modifier = Modifier.weight(1f),
                    title = "Gender",
                    subTitle = pet.gender.name
                )
                Spacer(modifier = Modifier.width(16.dp))

                val date = if (pet.birthDate != null) pet.birthDate.formatDate() else "Unknown"
                PetInfoItem(modifier = Modifier.weight(1f), title = "Birth date", subTitle = date)
            }

            Spacer(modifier = modifier.height(16.dp))

            // next task
            Text(
                text = "NEXT ACTION",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = LightPurple
            )
            Spacer(modifier = Modifier.height(8.dp))


            if (state.lastTask != null) {
                TaskCard(
                    task = state.lastTask,
                    onCheckedChange = {
                        onCheckedChanged(state.lastTask.id.toInt(), it)

                    }
                )
            } else {
                // empty state

                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .fillMaxWidth()
                        .background(CellsBgPurple)
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = SemiTransparentPurple)
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(16.dp),
                            painter = painterResource(R.drawable.ic_calendar_outlined),
                            contentDescription = "",
                            tint = LightPurple
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "No upcoming tasks.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightPurple
                    )
                    Text(
                        text = "Tap + to add one",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightPurple
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MainPurple),
                        onClick = onAddTaskClick) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "")
                            Text(
                                text = "Add task",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White
                            )

                        }
                    }

                }
            }
            if (pet.personality.isNotEmpty()) {
                Text(
                    text = "PERSONALITY",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = LightPurple
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pet.personality.forEach {
                        ChipButton(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            textColor = Color.White,
                            bgColor = MainPurple,
                            fontSize = 13.sp,
                            borderColor = MainPurple
                        ) {}
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // loading state
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()

            }
        }
    }
}

@Preview
@Composable
fun OverPreview() {
    PetPalTheme {
        Box(modifier = Modifier.background(Color.White)) {
            OverviewTab(
                state = DetailsState(),
                onCheckedChanged = { _, _ -> },
                onAddTaskClick = {}
            )
        }
    }
}
