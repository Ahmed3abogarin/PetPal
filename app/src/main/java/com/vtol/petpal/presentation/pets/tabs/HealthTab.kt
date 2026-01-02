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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.pets.components.AddWeightBottomSheet
import com.vtol.petpal.presentation.pets.components.AppChart
import com.vtol.petpal.presentation.pets.components.VetsList

@Composable
fun HealthTab(modifier: Modifier = Modifier, weightList: List<WeightRecord>, onAddWeightClicked: (WeightRecord) -> Unit) {

    var showBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Weight", style = MaterialTheme.typography.headlineMedium)
            FilledIconButton(
                onClick = {
                    showBottomSheet = true
                }, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
        Text(
            text = "Last updated: 13 minutes ago",
            color = Color.LightGray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        /* the weights represent the Y axis (vertical line)
           the dates represent the X axis (horizontal line)
         */

        AppChart(records = weightList.sortedBy { it.timestamp }.take(7))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Vet Visits", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        VetsList()
    }

    // The bottom sheet
    if (showBottomSheet){
        AddWeightBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSave = {
                onAddWeightClicked(it)
            }
        )
    }
}


@Preview
@Composable
fun HealthTabPreview() {
    HealthTab(weightList = listOf(
        WeightRecord(weight = 2.5, unit = WeightUnit.KG, timestamp = 1000000000000),
        WeightRecord(weight = 3.0, unit = WeightUnit.KG, timestamp = 1700003600000),
        WeightRecord(weight = 2.8, unit = WeightUnit.KG, timestamp = 1700007200000),
        WeightRecord(weight = 3.2, unit = WeightUnit.KG, timestamp = 1700010800000),
        WeightRecord(weight = 3.5, unit = WeightUnit.KG, timestamp = 1700014400000),
        WeightRecord(weight = 3.1, unit = WeightUnit.KG, timestamp = 1700018000000),
        WeightRecord(weight = 3.4, unit = WeightUnit.KG, timestamp = 1700021600000)
    )){}
}