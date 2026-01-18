package com.vtol.petpal.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {

    var visible by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = BackgroundColor
    )

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    Box(
        modifier = Modifier
            .background(bgColor)
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // app logo in the center

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                modifier = Modifier
                    .size(170.dp),
                painter = painterResource(R.drawable.app_splash_logo),
                contentDescription = "app logo"
            )
        }



        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopEnd),
            visible = visible,
            enter =
                expandIn(
                    // Overwrites the default spring animation with tween
                    animationSpec = tween(400, easing = LinearOutSlowInEasing),
                    // Overwrites the corner of the content that is first revealed
                    expandFrom = Alignment.BottomStart,
                ) {
                    // Overwrites the initial size to 50 pixels by 50 pixels
                    IntSize(50, 50)
                },
            exit =
                shrinkOut(
                    tween(100, easing = LinearOutSlowInEasing),
                    // Overwrites the area of the content that the shrink animation will end on. The
                    // following parameters will shrink the content's clip bounds from the full size of
                    // the
                    // content to 1/10 of the width and 1/5 of the height. The shrinking clip bounds
                    // will
                    // always be aligned to the CenterStart of the full-content bounds.
                    shrinkTowards = Alignment.CenterStart,
                ) { fullSize ->
                    // Overwrites the target size of the shrinking animation.
                    IntSize(fullSize.width / 10, fullSize.height / 5)
                },
        ) {

            // top right shape
            Image(
                modifier = Modifier
                    .height(110.dp)
                    .width(156.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.bg_shapes),
                contentDescription = "top right shape"
            )
        }


        // bottom left shape

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomStart),
            visible = visible,
            enter =
                expandIn(
                    // Overwrites the default spring animation with tween
                    animationSpec = tween(400, easing = LinearOutSlowInEasing),
                    // Overwrites the corner of the content that is first revealed
                    expandFrom = Alignment.BottomStart,
                ) {
                    // Overwrites the initial size to 50 pixels by 50 pixels
                    IntSize(50, 50)
                },
            exit =
                shrinkOut(
                    tween(100, easing = LinearOutSlowInEasing),
                    // Overwrites the area of the content that the shrink animation will end on. The
                    // following parameters will shrink the content's clip bounds from the full size of
                    // the
                    // content to 1/10 of the width and 1/5 of the height. The shrinking clip bounds
                    // will
                    // always be aligned to the CenterStart of the full-content bounds.
                    shrinkTowards = Alignment.CenterStart,
                ) { fullSize ->
                    // Overwrites the target size of the shrinking animation.
                    IntSize(fullSize.width / 10, fullSize.height / 5)
                },
        ) {

            Image(
                modifier = Modifier
                    .height(148.dp)
                    .width(130.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.splash_shape),
                contentDescription = "bottom left shape"
            )
        }

    }
}


@Preview
@Composable
fun SPreview() {
    PetPalTheme {
        SplashScreen()
    }
}