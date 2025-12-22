package com.vtol.petpal.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.PetPalTheme


@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    minLines: Int = 1,
    placeHolder: String,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        value = value,
        colors = filledTextFieldColors(),
        minLines = minLines ,
        onValueChange = { onValueChanged(it) },
        label = { Text(placeHolder) },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(10.dp))

    )
}

@Composable
fun filledTextFieldColors() = TextFieldDefaults.colors(
    disabledTextColor = Color.Black,
    disabledContainerColor = Color.White,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent

)

@Preview
@Composable
fun Ddsg(){
    PetPalTheme {
        AppTextField(value = "", placeHolder = "") { }
    }
}