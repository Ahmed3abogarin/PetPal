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
import androidx.compose.ui.unit.dp

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
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.White,
            focusedContainerColor = Color.White,
            disabledIndicatorColor = Color.Transparent
        ),
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