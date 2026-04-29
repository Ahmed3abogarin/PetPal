package com.vtol.petpal.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.ExtraLightPurple

@Composable
fun ChipButton(
    text: String,
    bgColor: Color = ExtraLightPurple.copy(alpha = 0.1f),
    textColor: Color,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 12.sp,
    borderColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(bgColor, CircleShape)
            .border(1.dp, color = borderColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)

    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = fontWeight,
            fontSize = fontSize
        )

    }
}