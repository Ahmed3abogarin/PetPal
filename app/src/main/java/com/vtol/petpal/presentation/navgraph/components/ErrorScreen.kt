package com.vtol.petpal.presentation.navgraph.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Red), contentAlignment = Alignment.Center){
        Text(text = message)
    }

}


@Preview
@Composable
fun MyPreView(){
    PetPalTheme {

        ErrorScreen("كيف يا فرط")

    }
}