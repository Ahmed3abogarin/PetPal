package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.vtol.petpal.domain.model.weight.WeightRange
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.presentation.pets.DetailsState
import com.vtol.petpal.presentation.pets.components.AddWeightBottomSheet
import com.vtol.petpal.presentation.pets.components.VetsList
import timber.log.Timber

@Composable
fun HealthTab(
    weightList: List<WeightRecord>,
    onAddWeightClicked: (WeightRecord) -> Unit,
    onRangedChanged: (WeightRange) -> Unit,
    state: DetailsState,
) {

    weightList.forEach {
        Timber.tag("WeightList").v(it.weight.toString())
    }
    var showBottomSheet by remember { mutableStateOf(false) }



    VetsList(
        state = state,
        weightList = weightList,
        onAddWeightClicked = {
            showBottomSheet = true
        },
        onRangeChanged = { onRangedChanged(it) }
    )


    // The bottom sheet
    if (showBottomSheet) {
        AddWeightBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSave = { record ->
                onAddWeightClicked(record)
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
            WeightRecord(weight = 2, timestamp = 1000000000000),
            WeightRecord(weight = 3, timestamp = 1700003600000),
            WeightRecord(weight = 2, timestamp = 1700007200000),
            WeightRecord(weight = 3, timestamp = 1700010800000),
            WeightRecord(weight = 3, timestamp = 1700014400000),
            WeightRecord(weight = 3, timestamp = 1700018000000),
            WeightRecord(weight = 3, timestamp = 1700021600000)
        ), onAddWeightClicked = {}, onRangedChanged = {})
}