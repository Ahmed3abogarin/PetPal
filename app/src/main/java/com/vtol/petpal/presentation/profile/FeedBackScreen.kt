package com.vtol.petpal.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.BackArrow
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.navgraph.Routes
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme


@Composable
fun FeedbackScreen(modifier: Modifier = Modifier, uiState: FeedbackUiState, navigateUp: () -> Unit) {

    when (uiState) {
        is FeedbackUiState.FeedbackForm -> FeedbackScreenContent(navigateUp = navigateUp)
        is FeedbackUiState.Loading -> {}
        is FeedbackUiState.Error -> {}
        is FeedbackUiState.Success -> FeedbackSuccessScreen(navigateUp = navigateUp)
    }
    
}


@Composable
fun FeedbackScreenContent(modifier: Modifier = Modifier, navigateUp: () -> Unit) {

    var rating by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }

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
                .padding(top = 6.dp, bottom = 12.dp)
        ) {

            BackArrow(
                modifier= Modifier.align(Alignment.CenterStart),
                tint = Color.White) {
                navigateUp()
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Send Feedback",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(64.dp))


        Text("Rate your experience 🐾")

        Spacer(Modifier.height(12.dp))

        StarRatingBar(
            rating = rating,
            onRatingChanged = { rating = it }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            shape = RoundedCornerShape(16.dp),
            minLines = 4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Write feedback...") }
        )

        Spacer(Modifier.height(20.dp))

        SaveButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Send Feedback", color = MainPurple
        ) {

        }
    }


}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $i",
                tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray,
                modifier = Modifier
                    .size(38.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}

@Preview
@Composable
fun FeedPreview() {
    PetPalTheme {
        FeedbackScreenContent(navigateUp = {})
    }
}