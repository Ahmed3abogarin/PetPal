package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.presentation.pets.components.AppChart
import com.vtol.petpal.presentation.pets.components.VetsList
import java.util.Date

@Composable
fun HealthTab(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Weight", style = MaterialTheme.typography.headlineMedium)
            FilledIconButton(onClick = {}, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = "Last updated: 13 minutes ago",
            color = Color.LightGray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppChart(
            records = listOf(
                WeightRecord(Date(), 5.0),
                WeightRecord(Date(), 6.0),
                WeightRecord(Date(), 1.4),
                WeightRecord(Date(), 1.4),
                WeightRecord(Date(), 2.0),
                WeightRecord(Date(), 1.4)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Vet Visits", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        VetsList()
    }
}



@Preview
@Composable
fun HealthTabPreview() {
    HealthTab()
}