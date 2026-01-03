package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightBottomSheet(
    onDismiss: () -> Unit,
    onSave: (WeightRecord) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var selectWUnit by remember { mutableStateOf(WeightUnit.KG) }

    var error by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Weight", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            PetTextField(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeHolder = "Weight",
                trailingIcon = Icons.Default.Face,
                value = weight,
                selectedUnit = selectWUnit,
                error = error,
                onTrailingClicked = {
                    val nextIndex =
                        (WeightUnit.entries.indexOf(selectWUnit) + 1) % WeightUnit.entries.size
                    selectWUnit = WeightUnit.entries[nextIndex]
                }
            ) { weight = it }

            Spacer(Modifier.height(16.dp))

            SaveButton(
                text = "Save",
                color = MainPurple,
                onClick = {
                    var isValid = true

                    error = if (weight.isBlank()) {
                        isValid = false
                        "This field cannot be empty"
                    } else null

                    if (!isValid) return@SaveButton

                    onSave(WeightRecord(weight = weight.toDouble(), unit = selectWUnit))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    PetPalTheme {
        AddWeightBottomSheet(onDismiss = {}, onSave = {})
    }

}