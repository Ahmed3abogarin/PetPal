package com.vtol.petpal.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R

@Composable
fun SummeryDashboard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(56.dp).padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_walk),
                    contentDescription = null
                )
                Text(text = "Walk", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock) ,
                    contentDescription = null
                )
                Text(text = "07:00AM", fontSize = 18.sp, modifier= Modifier.padding(start = 6.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(52.dp).padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_fed),
                    contentDescription = null
                )
                Text(text = "Feed", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock) ,
                    contentDescription = null
                )
                Text(text = "04:00PM", fontSize = 18.sp, modifier= Modifier.padding(start = 6.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(52.dp).padding(end = 8.dp),
                    painter = painterResource(R.drawable.ic_pill),
                    contentDescription = null
                )
                Text(text = "Medicine", fontSize = 22.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.ic_clock) ,
                    contentDescription = null
                )
                Text(text = "06:00PM", fontSize = 18.sp, modifier= Modifier.padding(start = 6.dp))
            }
        }
    }
}