package com.vtol.petpal.presentation.pets.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.R

@Composable
fun PetTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    label: String? = null,
    readOnly: Boolean = false,
    placeHolder: String,
    value: String,
    error: String? = null,
    minLines: Int = 1,
    iconSize: Dp = 32.dp,
    fontSize: TextUnit = 16.sp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    text: String? = null,
    @DrawableRes trailingIcon: Int? = null,
    @DrawableRes leadingIcon: Int? = null,
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
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                unfocusedBorderColor = Color(0XFFEBE5FF),
                focusedBorderColor = MainPurple
            ),
            visualTransformation = visualTransformation,
            isError = error != null,
            minLines = minLines,
            maxLines = if (minLines == 1) 1 else Int.MAX_VALUE,
            onValueChange = { onValueChanged(it) },
            placeholder = {
                Text(
                    text = placeHolder,
                    fontSize = fontSize,
                    color = LightPurple,
                    overflow = TextOverflow.Ellipsis
                )
            },
            shape = RoundedCornerShape(10.dp),
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(leadingIcon),
                        tint = LightPurple,
                        contentDescription = null
                    )
                }
            } else null,
            trailingIcon = if (trailingIcon != null) {
                {
                    IconButton(
                        onClick = {
                            onTrailingClicked?.invoke()
                        }
                    ) {
                        if (text == null){
                            Icon(
                                modifier = Modifier.size(iconSize),
                                painter = painterResource(trailingIcon),
                                tint = LightPurple,
                                contentDescription = null
                            )

                        } else{
                            Text(text, color = LightPurple)

                        }
                    }
                }
            } else null,
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
        PetTextField(
            placeHolder = "Pet Name", value = "",
            leadingIcon = R.drawable.ic_location, onValueChanged = {})
    }
}