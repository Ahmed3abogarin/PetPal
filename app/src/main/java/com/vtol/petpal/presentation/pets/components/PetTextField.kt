package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PetTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: String? = null,
    placeHolder: String,
    value: String,
    error: String? = null,
    selectedUnit: WeightUnit? = null,
    trailingIcon: ImageVector? = null,
    leadingIcon: ImageVector? = null,
    onTrailingClicked: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    Column(modifier) {
        label?.let {
            Text(
                modifier = Modifier.padding(bottom = 8.dp), text = it.uppercase(),
                fontSize = 14.sp,
                color = LightPurple,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                unfocusedBorderColor = Color(0XFFEBE5FF),
                focusedBorderColor = MainPurple
            ),
            isError = error != null,
            maxLines = 1,
            onValueChange = { onValueChanged(it) },
            placeholder = { Text(text = placeHolder, color = LightPurple) },
            shape = RoundedCornerShape(10.dp),
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(leadingIcon, contentDescription = null)
                }
            } else null,
            trailingIcon = {
                trailingIcon?.let {
                    IconButton(onClick = {
                        onTrailingClicked?.invoke()
                    }) {
                        Text(selectedUnit?.displayName ?: "", color = LightPurple)
                    }
                }
            },
            supportingText = if (error != null) {
                {
                    Text(text = error)

                }
            } else null,
            keyboardOptions = keyboardOptions
        )
    }

}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        PetTextField(placeHolder = "Pet Name", value = "", onValueChanged = {})
    }
}