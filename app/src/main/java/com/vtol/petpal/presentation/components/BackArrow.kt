package com.vtol.petpal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun BackArrow(modifier: Modifier = Modifier,tint: Color = Color.White, navigateUp: () -> Unit) {
    Card (
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        onClick = { navigateUp() }
    ) {
        Icon(
            modifier = Modifier.padding(14.dp).size(14.dp),
            painter = painterResource(R.drawable.ic_arrow) ,
            tint = tint,
            contentDescription = ""
        )
    }

}

@Preview
@Composable
fun ArrowPre(){
    PetPalTheme {
        Box(modifier = Modifier.fillMaxSize().background(MainPurple)){
            BackArrow(modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp,top =16.dp)) { }
        }

    }
}