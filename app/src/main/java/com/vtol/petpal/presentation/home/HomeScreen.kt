package com.vtol.petpal.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.home.components.ProgressCard
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen(onAddTaskClicked: () -> Unit, viewModel: HomeViewModel) {

//    Image(
//        painter = painterResource(R.drawable.header_img),
//        contentDescription = "",
//        modifier = Modifier.align(
//            Alignment.TopEnd
//        ).size(200.dp)
//    )

    Scaffold(
        floatingActionButton = {
            Column(
                Modifier
                    .clip(CircleShape)
                    .background(MainPurple)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable { onAddTaskClicked() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.White
                )

                Text(text = "Add Task", color = Color.White, fontSize = 8.sp)
            }

        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
                .background(Color(0XFFF8F4FF))


//            .verticalScroll(rememberScrollState())
        ) {

            // The header
            item {
                AppHomeHeader()
            }
//            AppHomeHeader()


            item {
                // Pets list
                Spacer(modifier = Modifier.height(16.dp))

                HomePetsList {}
                Spacer(modifier = Modifier.height(16.dp))

            }






            item {
                ProgressCard()
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Today",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                )

            }


            // Today's tasks
            items(viewModel.state.value.todayTasks) {
                TaskCard(it)

            }


            item {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Upcoming",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }


            // Upcoming tasks
            items(viewModel.state.value.upcomingTasks) {
                TaskCard(it)
            }

            item {
                Spacer(
                    modifier = Modifier
                        .statusBarsPadding()
                        .height(4.dp)
                )
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

@Composable
fun HomePetsList(modifier: Modifier = Modifier, onAddPetClicked: () -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(2) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.cat),
                    contentDescription = ""
                )
                Text(text = "Blind Pew")
            }
        }
        item {
            Box(
                modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MainPurple), contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onAddPetClicked() }) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = Color.White
                    )
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