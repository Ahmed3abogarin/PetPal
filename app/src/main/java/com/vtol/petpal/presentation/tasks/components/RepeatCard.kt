package com.vtol.petpal.presentation.tasks.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun RepeatCard(
    txt: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(1.dp, color = if (isSelected) MainPurple else LightPurple, shape = CircleShape)
            .clip(CircleShape)
            .background(if (isSelected) MainPurple else Color.White)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)

    ) {
        Text(
            text = txt,
            color = if (isSelected) Color.White else MainPurple,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}