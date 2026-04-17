package com.vtol.petpal.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.PetPalTheme


@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    minLines: Int = 1,
    colors: TextFieldColors = filledTextFieldColors(),
    placeHolder: String,
    errorTxt: String? = null,
    password: Boolean = false,
    isOneLine: Boolean = false,
    onValueChanged: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = value,
            colors = colors,
            minLines = minLines,
            onValueChange = { onValueChanged(it) },
            label = { Text(placeHolder) },
            shape = RoundedCornerShape(12.dp),
            isError = errorTxt != null,
            maxLines = if (isOneLine) 1 else Int.MAX_VALUE,
            supportingText = {
                errorTxt?.let {
                    Text(text = it)
                }
            },
            visualTransformation = if (password && !isPasswordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            trailingIcon = if (password) {
                {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible)
                                "Hide password"
                            else
                                "Show password"
                        )
                    }
                }
            } else null,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun filledTextFieldColors() = TextFieldDefaults.colors(
    disabledTextColor = Color.Black,
    disabledContainerColor = Color.White,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    errorContainerColor = Color.White,
    errorPlaceholderColor = Color.Gray,
    errorTextColor =  Color.Gray
)


@Preview
@Composable
fun Ddsg() {
    PetPalTheme {
        AppTextField(value = "", placeHolder = "", errorTxt = "Email Cannot be empty") { }
    }
}