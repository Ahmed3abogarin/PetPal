package com.vtol.petpal.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF8638FE))
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Hello Ahmed!", fontSize = 28.sp, color = Color.White)
                Row {
                    Icon(Icons.Default.LocationOn, contentDescription = "userLocation", tint = Color.White)
                    Text(text = "Jeddah | 35 | clear sky", fontSize = 12.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.height(10.dp))
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .shadow(elevation = 4.dp)
                .background(Color(0XFFF8F4FF))
                .padding(24.dp)

        ) {

            Text(text = "My Pets", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(4) {
                    Column {
                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            painter = painterResource(R.drawable.cat),
                            contentDescription = ""
                        )
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(Icons.Default.Build, contentDescription = null)
                        Text(text = "Walk")
                    }
                    Row {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Text(text = "10:00AM")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                        Text(text = "Walk")
                    }
                    Row {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Text(text = "04:00AM")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Text(text = "Walk")
                    }
                    Row {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Text(text = "08:00AM")
                    }
                }
            }
            Spacer(modifier= Modifier.height(18.dp))
            Text(text = "Actions", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)


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