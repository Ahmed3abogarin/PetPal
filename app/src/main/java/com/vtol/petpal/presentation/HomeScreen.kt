package com.vtol.petpal.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
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
import com.vtol.petpal.presentation.components.ActionsButton
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen() {

//    Image(
//        painter = painterResource(R.drawable.header_img),
//        contentDescription = "",
//        modifier = Modifier.align(
//            Alignment.TopEnd
//        ).size(200.dp)
//    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp))
                .background(Color(0XFF8638FE))
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








        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0XFFF8F4FF))
                .padding(24.dp)
        ) {

            Text(text = "My Pets", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(4) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Today's summery", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(14.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Build,
                            contentDescription = null
                        )
                        Text(text = "Walk", fontSize = 18.sp)
                    }
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                        Text(text = "10:00AM", fontSize = 18.sp)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                        Text(text = "Walk", fontSize = 18.sp)
                    }
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                        Text(text = "04:00AM", fontSize = 18.sp)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                        Text(text = "Walk", fontSize = 18.sp)
                    }
                    Row {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                        Text(text = "08:00AM", fontSize = 18.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Actions", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ActionsButton(
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    buttonText = "Add Food",
                    buttonIcon = Icons.Default.Notifications
                )
                ActionsButton(
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    buttonText = "Add Food",
                    buttonIcon = Icons.Default.Notifications
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ActionsButton(
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    buttonText = "Add Food",
                    buttonIcon = Icons.Default.Notifications
                )

                ActionsButton(
                    modifier = Modifier.weight(1f),
                    onClick = {},
                    buttonText = "Add Food",
                    buttonIcon = Icons.Default.Notifications
                )


            }


        }
    }


}

@Preview
@Composable
fun HomePreView() {
    PetPalTheme {
        HomeScreen()
    }
}