package com.vtol.petpal.presentation.pets.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun GalleryTab(modifier: Modifier = Modifier, isPremium: Boolean, onUpgradeClicked: () -> Unit) {

    if (isPremium){


    }else{
        Box(modifier= Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Upgrade to premium to access the gallery")
            FilledIconButton(onClick = { onUpgradeClicked() }) {
                Text(text = "Upgrade to Premium")
            }
        }
    }



}