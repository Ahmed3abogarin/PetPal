package com.vtol.petpal.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.UserViewModel
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.home.components.HomePetsList
import com.vtol.petpal.presentation.home.components.HomeScreenHeader
import com.vtol.petpal.presentation.home.components.HomeShimmer
import com.vtol.petpal.presentation.home.components.ProgressCard
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAddTaskClicked: () -> Unit,
    onAddPetClicked: () -> Unit,
    onPetClicked: (String) -> Unit,
    viewModel: HomeViewModel,
    userViewModel: UserViewModel
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val userState by userViewModel.state.collectAsState()

    val scaffoldState = remember { SnackbarHostState() }


    /*
    TODO:
      1- Fix the bottom padding in HomeScreen
      2- Review the required/non-required fields in Add task screen
      3- Make the check icon smaller in AddTaskScreen for the task type card
      4- Fix Vet Visit Card in Pet Screen
     */
    LaunchedEffect(state.value.error) {
        state.value.error?.let {
            scaffoldState.showSnackbar(it)
        }
    }


    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor,
        snackbarHost = { SnackbarHost(scaffoldState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            Column(
                Modifier
                    .clip(CircleShape)
                    .background(MainPurple)
                    .clickable {
                        if (state.value.petsList.isEmpty()){
                            Toast.makeText(context,"Add a pet first",Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        onAddTaskClicked()
                    }
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {

            // The header
            item {
                HomeScreenHeader(modifier = Modifier.statusBarsPadding().padding(top = 16.dp), state = userState)
            }


            item {
                // Pets list
                Spacer(modifier = Modifier.height(16.dp))

                val petsList = state.value.petsList

                HomePetsList(
                    pets = petsList,
                    onPetClicked = { onPetClicked(it) },
                    onAddPetClicked = {
                        if (petsList.size < 4) {
                            onAddPetClicked()
                        } else {
                            Toast.makeText(context, "Upgrade to premium", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
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
                            modifier = Modifier.height(160.dp),
                        )
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
                    }

                    // The tasks list (today)
                    items(state.value.todayTasks) { task ->
                        TaskCard(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            task = task,
                            petName = state.value.petMap[task.petId] ?: "Unknown",
                            onCheckedChange = {
                                viewModel.toggleCompletion(task.id.toInt(), it)
                            }
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
                    }
                }


                // the list of tasks
                val tasks = state.value.upcomingTasks
                items(tasks) { task ->
                    TaskCard(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        task = task,
                        petName = state.value.petMap[task.petId] ?: "Unknown",
                        onCheckedChange = {
                            viewModel.toggleCompletion(task.id.toInt(), it)
                        }
                    )
                    if (tasks.last() == task){
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }

        if (state.value.isLoading) {
            HomeShimmer()
        }
    }
}

@Preview
@Composable
fun HomePreView() {
    PetPalTheme {

    }
}