package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.pets.DetailsState
import com.vtol.petpal.presentation.pets.components.AddWeightBottomSheet
import com.vtol.petpal.presentation.pets.components.VetsList

@Composable
fun HealthTab(
    weightList: List<WeightRecord>,
    onAddWeightClicked: (WeightRecord) -> Unit,
    state: DetailsState,
) {

    var showBottomSheet by remember { mutableStateOf(false) }



    VetsList(state = state,weightList = weightList, onAddWeightClicked = {
        showBottomSheet = true
    })

    // The bottom sheet
    if (showBottomSheet) {
        AddWeightBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSave = {
                onAddWeightClicked(it)
                showBottomSheet = false
            }
        )
    }
}


@Preview
@Composable
fun HealthTabPreview() {
    HealthTab(
        state = DetailsState(),
        weightList = listOf(
            WeightRecord(weight = 2.5, unit = WeightUnit.KG, timestamp = 1000000000000),
            WeightRecord(weight = 3.0, unit = WeightUnit.KG, timestamp = 1700003600000),
            WeightRecord(weight = 2.8, unit = WeightUnit.KG, timestamp = 1700007200000),
            WeightRecord(weight = 3.2, unit = WeightUnit.KG, timestamp = 1700010800000),
            WeightRecord(weight = 3.5, unit = WeightUnit.KG, timestamp = 1700014400000),
            WeightRecord(weight = 3.1, unit = WeightUnit.KG, timestamp = 1700018000000),
            WeightRecord(weight = 3.4, unit = WeightUnit.KG, timestamp = 1700021600000)
        ), onAddWeightClicked = {})
}