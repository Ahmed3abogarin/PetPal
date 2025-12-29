package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun OverviewTab(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.background(Color.White)) {
        Spacer(modifier = modifier.height(16.dp))
        Text(text = "Upcoming tasks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = modifier.height(8.dp))
//        TasksList()
        Spacer(modifier = modifier.height(8.dp))


        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LightPurple)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pet Information", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Last updated: 13 minutes ago", color = Color.LightGray, fontSize = 12.sp)
            }
            Spacer(modifier= Modifier.height(6.dp))
            InfoItem("Name: Blind Pew","Gender: Male")
            Spacer(modifier= Modifier.height(6.dp))
            InfoItem("Breed: Shiraz Cat","Weight: 750 L")
            Spacer(modifier= Modifier.height(6.dp))
            InfoItem("Birth date: 2022/12/2","Color: Orange")
            Spacer(modifier = modifier.height(8.dp))
        }
    }
}
@Composable
fun InfoItem(firstTxt: String, secondTxt: String){
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = firstTxt, color = Color.Black, fontSize = 14.sp)
        Text(text = secondTxt, color = Color.Black,fontSize = 14.sp)
    }
}
@Preview(device = PIXEL_7_PRO)
@Composable
fun OverviewPreview(modifier: Modifier = Modifier) {
    PetPalTheme {
        OverviewTab()
    }
    
}