package com.vtol.petpal.presentation.explore.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PulsingCircle() {
    val infiniteTransition = rememberInfiniteTransition()

    val sizes = listOf(60.dp, 70.dp, 80.dp)

    val scaleFactors = List(sizes.size) { index ->
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.4f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 6000
                    0.0f at 0 using LinearEasing
                    0.2f at (1000 + (index * 1000)) using LinearEasing
                    1.2f at (2000 + (index * 1000)) using LinearEasing
                    1.4f at (4000 + (index * 1000)) using LinearEasing
                    1.6f at 6000 using LinearEasing

                },
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val alphaValues = List(sizes.size) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 6000
                    0.0f at 0 using LinearEasing
                    0.2f at (2000 + (index * 1000)) using LinearEasing
                    0.1f at (4000 + (index * 1000)) using LinearEasing
                    0.0f at 6000 using LinearEasing

                },
                repeatMode = RepeatMode.Restart
            )
        )
    }




    Canvas(modifier = Modifier.size(250.dp)) {
        sizes.forEachIndexed { index, size ->
            val scale = scaleFactors[index].value
            val alpha = alphaValues[index].value
            val radius = size.toPx() / 2


            drawCircle(
                color = MainPurple,
                radius = radius * scale,
                center = center,
                alpha = alpha
            )
        }
    }
}


@Preview
@Composable
fun GPSNotGrantedScreenPreview() {
    PetPalTheme {
        PulsingCircle()
    }
}