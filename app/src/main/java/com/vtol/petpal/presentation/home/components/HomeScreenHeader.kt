package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.util.showToast

@Composable
fun HomeScreenHeader(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    userName: String?
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.greeting_anim),
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        restartOnPlay = false
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MainPurple),
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        val context = LocalContext.current
        Column(modifier = modifier.padding(horizontal = 12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(34.dp),
                            painter = painterResource(R.drawable.ic_logo),
                            contentDescription = ""
                        )
                    }

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
                    when  {
                        isLoading -> {
                            Text(
                                text = "Loading...",
                                fontSize = 28.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        userName != null -> {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    text = userName,
                                    fontSize = 28.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                LottieAnimation(
                                    modifier = Modifier.size(38.dp),
                                    composition = composition,
                                    progress = { progress }
                                )
                            }
                        }

                        else -> {
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
            }
        }
    }
}