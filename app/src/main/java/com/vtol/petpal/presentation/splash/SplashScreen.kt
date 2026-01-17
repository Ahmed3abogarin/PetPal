package com.vtol.petpal.presentation.splash

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun SplashScreen() {

    val bgColor by animateColorAsState(
        targetValue = BackgroundColor
    )

    Box(
        modifier = Modifier
            .background(bgColor)
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // app logo in the center
        A
        Image(
            modifier = Modifier
                .size(170.dp)
                .align(Alignment.Center),
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "app logo"
        )


        // top right shape
        Image(
            modifier = Modifier
                .height(110.dp).width(156.dp)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.bg_shapes),
            contentDescription = "top right shape"
        )


        // bottom left shape
        Image(
            modifier = Modifier
                .height(148.dp).width(130.dp)
                .align(Alignment.BottomStart),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.splash_shape),
            contentDescription = "bottom left shape"
        )
    }
}


@Preview
@Composable
fun SPreview() {
    PetPalTheme {
        SplashScreen()
    }
}