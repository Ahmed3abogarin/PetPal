package com.vtol.petpal.presentation.add_pet.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.formatDate

@Composable
fun PetDateTextField(date: Long?, onClick: () -> Unit) {
    Column {

        Text(
            text = "Gender",
            fontSize = 14.sp,
            color = LightPurple,
            fontWeight = FontWeight.Medium
        )
        // Birth date picker
        OutlinedTextField(
            value = date?.formatDate() ?: "",
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = LightPurple,
                disabledLabelColor = LightPurple,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            ),
            onValueChange = {},
            placeholder = { Text("DD / MM / YYYY") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onClick()
                },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar_outlined),
                    contentDescription = null,
                    tint = LightPurple
                )
            }
        )
    }
}