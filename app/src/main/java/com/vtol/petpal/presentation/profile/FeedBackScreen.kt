package com.vtol.petpal.presentation.profile

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FieldValue
import com.vtol.petpal.presentation.common.components.ChipButton
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.profile.components.StarRatingBar
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.getVersionName
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun FeedbackScreen(
    state: FeedbackUiState,
    onSubmitClick: (HashMap<String, Any>) -> Unit,
    navigateUp: () -> Unit
) {
    when (state) {
        is FeedbackUiState.FeedbackForm -> FeedbackScreenContent(
            onSubmitClick = onSubmitClick,
            navigateUp = navigateUp
        )

        is FeedbackUiState.Loading -> {
            LoadingIndicator()
        }

        is FeedbackUiState.Error -> {
            Text(text = "Something went wrong")
            LaunchedEffect(Unit) {
                delay(2000)
                navigateUp()
            }
        }

        is FeedbackUiState.Success -> FeedbackSuccessScreen(navigateUp = navigateUp)
    }

}


@Composable
fun FeedbackScreenContent(onSubmitClick: (HashMap<String, Any>) -> Unit, navigateUp: () -> Unit) {

    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val country = Locale.getDefault().displayCountry
    val language = Locale.getDefault().displayLanguage

    val deviceModel = Build.MANUFACTURER + " " + Build.MODEL
    val androidVersion = Build.VERSION.RELEASE


    var rating by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainPurple)
                    .statusBarsPadding()
                    .padding(top = 16.dp, bottom = 62.dp, start = 16.dp)
            ) {

                AppIconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    tint = Color.White
                ) {
                    navigateUp()
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Send Feedback",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "Help us improve your experience",
                        color = ExtraLightPurple
                    )

                }
            }


            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-28).dp),
                color = Color.White,
                shadowElevation = 3.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 38.dp, horizontal = 16.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        "How was your experience?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MainPurple
                    )

                    Spacer(Modifier.height(18.dp))

                    StarRatingBar(
                        rating = rating,
                        onRatingChanged = { rating = it }
                    )

                    Spacer(Modifier.height(18.dp))

                    val tags =
                        listOf("Easy to use", "Fast", "Helpful", "Confusing", "Missing features")

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = 3,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        tags.forEach {
                            val isSelected = tag == it
                            ChipButton(
                                text = it,
                                textColor = if (isSelected) MainPurple else ExtraLightPurple,
                                borderColor = if (isSelected) MainPurple else ExtraLightPurple
                            ) {
                                tag = it
                            }
                        }
                    }


                    Spacer(Modifier.height(18.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = {
                            if (it.length <= 300) {
                                message = it
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        minLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = ExtraLightPurple.copy(alpha = 0.1f),
                            focusedContainerColor = ExtraLightPurple.copy(alpha = 0.1f),
                            focusedBorderColor = MainPurple,
                            unfocusedBorderColor = ExtraLightPurple
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Tell us more (optional)...",
                                color = ExtraLightPurple
                            )
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    SaveButton(
                        text = "Send Feedback", color = MainPurple,
                        enabled = rating > 0
                    ) {

                        val feedback = hashMapOf(
                            "message" to message,
                            "rating" to rating.toString(),
                            "tag" to tag,
                            "appVersion" to context.getVersionName(),
                            "androidVersion" to androidVersion,
                            "deviceModel" to deviceModel,
                            "country" to country,
                            "language" to language,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                        onSubmitClick(feedback)
                    }

                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = "Your response is anonymous",
                        fontSize = 13.sp,
                        color = LightPurple
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FeedPreview() {
    PetPalTheme {
        FeedbackScreenContent(navigateUp = {}, onSubmitClick = {})
    }
}