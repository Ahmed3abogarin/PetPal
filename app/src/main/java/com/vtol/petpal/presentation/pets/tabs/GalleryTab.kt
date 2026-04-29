package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.DarkGold
import com.vtol.petpal.ui.theme.Gold
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.ui.theme.TextPurple

@Composable
fun GalleryTab(isPremium: Boolean, onUpgradeClicked: () -> Unit) {

    if (isPremium) {


    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Upgrade to premium to access the gallery", fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = TextPurple
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold),
                    onClick = { onUpgradeClicked() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Upgrade", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = CircleShape,
                            color = DarkGold,
                            border = BorderStroke(width = 2.dp, color = Color.White)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(6.dp),
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = "icon"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun GalleryPreview() {
    PetPalTheme {
        GalleryTab(isPremium = false, onUpgradeClicked = {})

    }
}