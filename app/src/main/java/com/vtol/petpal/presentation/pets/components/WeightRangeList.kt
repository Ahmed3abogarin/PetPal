package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.weight.WeightRange
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.TextPurple

@Composable
fun WeightRangeList(
    selectedRange: WeightRange,
    onRangeClick: (WeightRange) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        WeightRange.entries.forEach { range ->

            val selected = selectedRange == range

            Card(
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) MainPurple else CellsBgPurple,
                    contentColor = if (selected) Color.White else TextPurple
                ),
                onClick = {
                    onRangeClick(range)
                }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 3.dp),
                    fontSize = 11.sp,
                    text = when (range) {
                        WeightRange.DAYS_7 -> "7D"
                        WeightRange.DAYS_30 -> "30D"
                        WeightRange.MONTHS_6 -> "6M"
                        WeightRange.YEAR_1 -> "1Y"
                        WeightRange.ALL -> "All"
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    PetPalTheme {

        WeightRangeList(
            selectedRange = WeightRange.MONTHS_6
        ) {}
    }
}