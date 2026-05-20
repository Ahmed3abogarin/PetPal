package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.TextPurple
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.rememberShimmerBrush
@Composable
fun ProfileShimmerEffect(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()
    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        Column {
            // Top content
            Column(
                modifier = Modifier
                    .background(petPalGradient)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 32.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "My Profile",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .size(38.dp)
                            .background(brush)
                            .align(Alignment.CenterEnd)
                    )


                }

                Spacer(modifier = Modifier.height(8.dp))

                // User Image + add icon button
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(brush)
                        .size(110.dp),
                )


                // Username
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(brush)
                        .width(80.dp)
                        .height(6.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // User email
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(brush)
                        .width(140.dp)
                        .height(8.dp)
                )

            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Card(
            modifier = modifier
                .offset(y = (-24).dp)
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .background(brush)
                            .size(24.dp)
                    )


                    Text(
                        text = "Pets",
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .alpha(0.5f)
                        .padding(vertical = 22.dp)
                        .background(MainPurple)
                )


                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .background(brush)
                            .size(24.dp)
                    )


                    Text(
                        text = "Tasks done",
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .alpha(0.5f)
                        .padding(vertical = 22.dp)
                        .background(MainPurple)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .background(brush)
                            .size(24.dp)
                    )

                    Text(
                        text = "Vet visits",
                        fontSize = 14.sp,
                        color = TextPurple
                    )

                }
            }
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "PREFERENCES",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {

            SettingsButtonShimmerEffect(brush = brush)


            SettingsButtonShimmerEffect(brush = brush)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButtonShimmerEffect(brush = brush)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButtonShimmerEffect(brush = brush)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            text = "SUPPORT",
            fontSize = 15.sp,
            color = MainPurple,
            fontWeight = FontWeight.W400
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
        ) {
            SettingsButtonShimmerEffect(brush = brush)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )

            SettingsButtonShimmerEffect(brush = brush)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButtonShimmerEffect(brush = brush)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MainPurple.copy(0.3f),
                thickness = 0.2.dp
            )
            SettingsButtonShimmerEffect(brush = brush)
        }
    }
}

@Composable
fun SettingsButtonShimmerEffect(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(brush)
                    .padding(14.dp)
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(brush)
                    .width(80.dp)
                    .height(8.dp)
            )

        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            tint = MainPurple.copy(alpha = 0.3f),
            contentDescription = ""
        )
    }
}


@Preview
@Composable
fun ProfileShimmerPreview() {
    PetPalTheme {
        ProfileShimmerEffect()
    }
}