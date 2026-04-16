package com.vtol.petpal.presentation.home

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.core.net.toUri
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.UserViewModel
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.home.components.HomePetsList
import com.vtol.petpal.presentation.home.components.HomeScreenHeader
import com.vtol.petpal.presentation.home.components.ProgressCard
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen(
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

    LaunchedEffect(state.value.error) {
        state.value.error?.let {
            scaffoldState.showSnackbar(it)
        }
    }

    LaunchedEffect(viewModel.permissionRequired) {
        requestExactAlarmPermissionIfNeeded(context)
    }

//    Image(
//        painter = painterResource(R.drawable.header_img),
//        contentDescription = "",
//        modifier = Modifier.align(
//            Alignment.TopEnd
//        ).size(200.dp)
//    )

    Scaffold(
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
                .fillMaxSize()
                .background(Color(0XFFF8F4FF))
        ) {


            // The header
            item {
                HomeScreenHeader(state = userState)
            }
//            AppHomeHeader()


            item {
                // Pets list
                Spacer(modifier = Modifier.height(16.dp))

                HomePetsList(
                    pets = state.value.petsList,
                    onPetClicked = { onPetClicked(it) },
                    onAddPetClicked = { onAddPetClicked() })
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
                            task = it,
                            petName = state.value.petMap[it.petId] ?: "Unknown"
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
                        task = it,
                        petName = state.value.petMap[it.petId] ?: "Unknown"
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(paddingValues.calculateBottomPadding())
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

fun requestExactAlarmPermissionIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        }
    }
}

@Preview
@Composable
fun HomePreView() {
    PetPalTheme {

    }
}