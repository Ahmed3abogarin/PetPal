package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.Red
import com.vtol.petpal.util.getGreeting

@Composable
fun HomeScreenHeader(
    modifier: Modifier = Modifier,
    showBadge: Boolean,
    isLoading: Boolean,
    userName: String?,
    userImg: String?,
    navigateToActionCenter: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.greeting_anim),
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        restartOnPlay = false
    )


    Column(
        modifier = modifier
            .background(BackgroundColor)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Image(
                    modifier = Modifier
                        .size(34.dp),
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null
                )

                Text(
                    text = "PetPal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MainPurple
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { navigateToActionCenter() }
                            .background(Color.White)
                            .padding(10.dp)
                            .size(18.dp),
                        painter = painterResource(R.drawable.ic_bell),
                        contentDescription = null
                    )

                    if (showBadge) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-9).dp, x = (10).dp)
                                .align(Alignment.BottomStart)
                                .clip(CircleShape)
                                .background(Red)
                                .size(6.dp)
                        )
                    }
                }

                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(38.dp),
                    model = ImageRequest.Builder(LocalContext.current).data(userImg).build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "user image",
                    placeholder = painterResource(R.drawable.img_profile_ph)
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Column(horizontalAlignment = Alignment.Start) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                when {
                    isLoading -> {
                        Text(
                            text = "Loading...",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }

                    userName != null -> {
                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = "${getGreeting()}, $userName!",
                            fontSize = 24.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                        LottieAnimation(
                            modifier = Modifier.size(38.dp),
                            composition = composition,
                            progress = { progress }
                        )
                    }

                    else -> Unit
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Here is what’s happening with your pets today",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun HeaderPreview() {
    PetPalTheme {
        HomeScreenHeader(
            showBadge = true,
            isLoading = false,
            userName = "Ahmed",
            userImg = "",
            navigateToActionCenter = {}
        )
    }
}