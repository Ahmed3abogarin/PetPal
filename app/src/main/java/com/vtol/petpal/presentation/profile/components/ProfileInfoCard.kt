package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun ProfileInfoCard(
    modifier: Modifier = Modifier,
    petsCount: Int,
    doneTasks: Int,
    vetVisits: Int
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(0.3.dp, MainPurple.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = petsCount.toString(),
                    fontSize = 20.sp,
                    color = MainPurple,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Pets",
                    fontSize = 16.sp,
                    color = Color.Black
                )

            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .alpha(0.5f)
                    .padding(vertical = 22.dp)
                    .background(MainPurple)
            )


            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = doneTasks.toString(),
                    fontSize = 20.sp,
                    color = MainPurple
                )

                Text(
                    text = "Tasks done",
                    fontSize = 16.sp,
                    color = Color.Black
                )

            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .alpha(0.5f)
                    .padding(vertical = 22.dp)
                    .background(MainPurple)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = vetVisits.toString(),
                    fontSize = 20.sp,
                    color = MainPurple
                )

                Text(
                    text = "Vet visits",
                    fontSize = 16.sp,
                    color = Color.Black
                )

            }


        }
    }
}