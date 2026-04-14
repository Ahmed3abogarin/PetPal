package com.vtol.petpal.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.BackgroundColor
import kotlinx.coroutines.delay

@Composable
fun FeedbackSuccessScreen(modifier: Modifier = Modifier, navigateUp: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.success_anim)
    )

    LaunchedEffect(Unit) {
        delay(2500)
        navigateUp()
    }

    Column(
        modifier= Modifier.fillMaxSize().background(BackgroundColor).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier.size(350.dp),
            composition = composition
        )

//        Spacer(modifier = Modifier.height(18.dp))

        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            text = "Thanks for your feedback \uD83D\uDC3E\n\nSubmitted successfully "
        )


    }




}


@Preview
@Composable
fun FeedbackSuccessScreenP(modifier: Modifier = Modifier) {
    FeedbackSuccessScreen(){}
}
