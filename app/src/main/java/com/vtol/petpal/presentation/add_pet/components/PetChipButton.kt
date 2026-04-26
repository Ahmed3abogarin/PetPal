package com.vtol.petpal.presentation.add_pet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PetChipButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    icon: Int,
    txt: String,
    tint: Color,
    padding: Dp = 3.dp,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = colors,
        border = BorderStroke(1.dp, color = Color(0XFFEBE5FF)),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(vertical = padding)
                    .size(16.dp),
                tint = tint,
                painter = painterResource(icon), contentDescription = null
            )

            Text(
                text = txt,
                color = tint
            )
        }
    }
}