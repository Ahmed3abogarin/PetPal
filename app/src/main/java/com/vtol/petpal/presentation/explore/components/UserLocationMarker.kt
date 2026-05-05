package com.vtol.petpal.presentation.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun UserLocationMarker(state: MarkerState) {
    MarkerComposable(
        state = state,
        anchor = Offset(0.5f, 0.5f) // centers the marker
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .background(MainPurple, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = "Your location",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}