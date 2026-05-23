package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun GalleryTab(isPremium: Boolean, onUpgradeClicked: () -> Unit) {

    if (isPremium) {


    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.blur(7.dp)
            ){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.gallery_upgrade_img),
                    contentDescription = "gallery upgrade image",
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f))
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.clip(CircleShape).background(Color.White)
                ){
                    Icon(
                        modifier = Modifier.size(92.dp).padding(24.dp),
                        painter = painterResource(R.drawable.ic_lock_filled),
                        contentDescription = null,
                        tint = MainPurple
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Premium Feature",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 64.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 14.sp,
                    text = "Gallery is a premium feature.\nUpgrade to unlock and access all your pet memories."
                )
                Spacer(modifier = Modifier.height(38.dp))
                SaveButton(
                    modifier = Modifier.padding(horizontal = 40.dp),
                    onClick = onUpgradeClicked,
                    text = "Upgrade to Premium",
                    color = MainPurple
                )
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