package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.SummeryDashboard
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun OverviewTab(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = modifier.height(16.dp))
        Text(text = "Upcoming tasks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = modifier.height(8.dp))
        SummeryDashboard()
        Spacer(modifier = modifier.height(8.dp))


        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .background(LightPurple)
                .padding(10.dp)
        ) {
            Text(text = "Pet", style = MaterialTheme.typography.headlineMedium)


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