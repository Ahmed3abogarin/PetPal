package com.vtol.petpal.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.home.components.HomePetsList
import com.vtol.petpal.presentation.home.components.ProgressCard
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen(onAddTaskClicked: () -> Unit, viewModel: HomeViewModel) {
    val state = viewModel.state.collectAsState()

    val scaffoldState = remember { SnackbarHostState() }

    LaunchedEffect(state.value.error) {
        state.value.error?.let {
            scaffoldState.showSnackbar(it)
        }
    }

//    Image(
//        painter = painterResource(R.drawable.header_img),
//        contentDescription = "",
//        modifier = Modifier.align(
//            Alignment.TopEnd
//        ).size(200.dp)
//    )

    Scaffold (
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            Column(
                Modifier
                    .clip(CircleShape)
                    .background(MainPurple)
                    .clickable { onAddTaskClicked() }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.White
                )
                Text(text = "Add Task", color = Color.White, fontSize = 8.sp)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
                .background(Color(0XFFF8F4FF))
        ) {

            // The header
            item {
                AppHomeHeader()
            }
//            AppHomeHeader()


            item {
                // Pets list
                Spacer(modifier = Modifier.height(16.dp))

                HomePetsList(pets = state.value.petsList, onAddPetClicked = {})
                Spacer(modifier = Modifier.height(16.dp))

            }


            item {
                ProgressCard(
                    progress = state.value.progress,
                    total = state.value.total,
                    completed = state.value.completedCount,
                    percentage = state.value.percentage
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            // check if both lists are empty
            if (state.value.todayTasks.isEmpty() && state.value.upcomingTasks.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.empty_state_image),
                            contentDescription = "No tasks",
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tasks yet!\nAdd a task to get started.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

            } else {
                if (state.value.todayTasks.isNotEmpty()) {

                    // Today's tasks
                    // the header
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Today",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // The tasks list (today)
                    items(state.value.todayTasks) {
                        TaskCard(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            task = it
                        )
                    }
                }

                // Upcoming tasks
                if (state.value.upcomingTasks.isNotEmpty()) {
                    // The header
                    item {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Upcoming",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }


                // the list of tasks
                items(state.value.upcomingTasks) {
                    TaskCard(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        task = it
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(4.dp)
                )
            }
        }

        if (state.value.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun AppHomeHeader(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MainPurple),
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 12.dp)

        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 8.dp),
                        painter = painterResource(R.drawable.logo),
                        contentDescription = ""
                    )
                    Text(text = "PetPal", fontSize = 24.sp, color = Color.White)
                }

                OutlinedCard(
                    onClick = {},
                    shape = CircleShape,
                    border = BorderStroke(width = 1.dp, color = Color.White),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f))
                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        imageVector = Icons.Default.Notifications,
                        tint = Color.White,
                        contentDescription = ""
                    )

                }


            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Hello,", fontSize = 28.sp, color = Color.White)
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = "Ahmed Adil",
                        fontSize = 28.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Card(shape = CircleShape) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(text = "35° C")
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(R.drawable.weather_icon),
                            contentDescription = "weather icon"
                        )
                    }
                }
            }

        }


    }

}


@Preview
@Composable
fun HomePreView() {
    PetPalTheme {

    }
}