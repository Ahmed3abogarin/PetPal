package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun ProgressCard(modifier: Modifier = Modifier) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (modifier = modifier.clip(RoundedCornerShape(12.dp)).weight(1f)){
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            ) {

                drawRect(
                    color = Color.LightGray ,
                    size = size
                )

                drawRect(
                    color = MainPurple,
                    size = Size(
                        width = size.width * 0.4f.coerceIn(0f, 1f),
                        height = size.height
                    )
                )

            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "40%",
            style = MaterialTheme.typography.labelLarge
        )


    }
}