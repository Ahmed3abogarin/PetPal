package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple


@Composable
fun ProgressCard(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // bg shapes
            Image(
                modifier = Modifier
                    .width(112.dp)
                    .height(80.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(R.drawable.bg_shapes),
                contentDescription = ""
            )

            // Progress image
            Image(
                modifier = Modifier
                    .width(112.dp)
                    .height(80.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(R.drawable.progress_img),
                contentDescription = ""
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "Today's Progress",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                    color = MainPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "8/14 Completed Tasks",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Progress card
                ProgressLinearBar()
            }
        }
    }
}


@Composable
fun ProgressLinearBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .weight(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            ) {

                drawRect(
                    color = Color.LightGray,
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