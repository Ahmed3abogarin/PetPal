package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PetTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions? = null,
    placeHolder: String,
    value: String,
    selectedUnit: WeightUnit? = null,
    trailingIcon: ImageVector? = null,
    onTrailingClicked: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        value = value,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = MainPurple
        ),
        maxLines = 1,
        onValueChange = { onValueChanged(it) },
        placeholder = { Text(text = placeHolder, color = Color.LightGray) },
        shape = RoundedCornerShape(10.dp),
        trailingIcon = {
            trailingIcon?.let {
                IconButton(onClick = {
                    onTrailingClicked?.invoke()
                }) {
                    Text(selectedUnit?.displayName ?: "")
                }
            }

        },
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default
    )

}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        PetTextField(placeHolder = "Pet Name", value = "", onValueChanged = {})
    }
}