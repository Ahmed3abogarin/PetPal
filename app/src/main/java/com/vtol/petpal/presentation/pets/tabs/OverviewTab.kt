package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.presentation.components.SummeryDashboard
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun OverviewTab(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Spacer(modifier = modifier.height(16.dp))
        Text(text = "Upcoming tasks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = modifier.height(8.dp))
        SummeryDashboard()
        Spacer(modifier = modifier.height(8.dp))


        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightPurple)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pet Information", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Last updated: 13 minutes ago", color = Color.LightGray, fontSize = 12.sp)
            }
            Spacer(modifier= Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Text1", color = Color.Black)
                Text(text = "Text2", color = Color.Black)
            }
            Spacer(modifier= Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Text1", color = Color.Black)
                Text(text = "Text2", color = Color.Black)
            }
            Spacer(modifier= Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Text1", color = Color.Black)
                Text(text = "Text2", color = Color.Black)
            }
            Spacer(modifier = modifier.height(8.dp))
        }

    }
}

@Preview
@Composable
fun OverviewPreview(modifier: Modifier = Modifier) {
    PetPalTheme {
        OverviewTab()
    }
    
}