package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.TaskCard
import com.vtol.petpal.presentation.pets.DetailsState
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun OverviewTab(modifier: Modifier = Modifier, state: DetailsState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Row {
            InfoItem(modifier = Modifier.weight(1f), title = "Breed", subTitle = "Shirazi")
            Spacer(modifier = Modifier.width(16.dp))
            InfoItem(modifier = Modifier.weight(1f), title = "Weight", subTitle = "20 kg")
        }
        Spacer(modifier = modifier.height(16.dp))

        Row {
            InfoItem(modifier = Modifier.weight(1f), title = "Gender", subTitle = "Male")
            Spacer(modifier = Modifier.width(16.dp))
            InfoItem(modifier = Modifier.weight(1f), title = "Birth date", subTitle = "02.12.2025")
        }

        Spacer(modifier = modifier.height(16.dp))

        // next task
        Text(text = "Next action", style = MaterialTheme.typography.headlineMedium)


        state.nextTask?.let {
            TaskCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), task = it)
        }


        // empty state
        if (state.nextTask == null){
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No data",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                )
            }

        }



        // loading state
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()

            }
        }


    }
}

@Composable
fun InfoItem(modifier: Modifier = Modifier, title: String, subTitle: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurple)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

        }

    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
fun OverviewPreview() {
    PetPalTheme {
        OverviewTab(state = DetailsState())
    }

}