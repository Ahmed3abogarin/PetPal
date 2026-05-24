package com.vtol.petpal.presentation.tasks.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun TaskTypeCard(
    task: String,
    @DrawableRes icon: Int,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MainPurple else ExtraLightPurple
    )

    val cellColor by animateColorAsState(
        targetValue = if (isSelected) MainPurple else Color.White
    )

    val txtColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MainPurple
    )

    Card(
        onClick = onSelected,
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = CardDefaults.cardColors(containerColor = cellColor)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .width(72.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(icon),
                    contentDescription = ""
                )
                Text(
                    text = task,
                    color = txtColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                modifier = Modifier
                    .padding(2.dp)
                    .size(18.dp)
                    .align(Alignment.TopEnd),
                tint = Color.White,
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null
            )
        }
    }
}