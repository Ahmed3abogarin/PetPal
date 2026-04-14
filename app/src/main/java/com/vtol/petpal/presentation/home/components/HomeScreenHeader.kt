package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.util.Resource
import com.vtol.petpal.util.showToast

@Composable
fun HomeScreenHeader(modifier: Modifier = Modifier, state: Resource<User>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MainPurple),
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        val context = LocalContext.current
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
                    onClick = {
                        context.showToast()
                    },
                    shape = CircleShape,
                    border = BorderStroke(width = 1.dp, color = Color.White),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f))
                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        imageVector = Icons.Default.Notifications,
                        tint = Color.White,
                        contentDescription = "notification icon"
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
                    when (state) {
                        is Resource.Loading -> {
                            Text(
                                text = "Loading...",
                                fontSize = 28.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        is Resource.Success -> {
                            Text(
                                modifier = Modifier.padding(start = 3.dp),
                                text = state.data.name,
                                fontSize = 28.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        is Resource.Error -> {
                            Text(
                                modifier = Modifier.padding(start = 3.dp),
                                text = "Error",
                                fontSize = 28.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // TODO: add the weather api

//                Card(shape = CircleShape) {
//                    Row(modifier = Modifier.padding(16.dp)) {
//                        Text(text = "35° C")
//                        Spacer(modifier = Modifier.width(6.dp))
//                        Image(
//                            painter = painterResource(R.drawable.weather_icon),
//                            contentDescription = "weather icon"
//                        )
//                    }
//                }
            }

        }


    }

}