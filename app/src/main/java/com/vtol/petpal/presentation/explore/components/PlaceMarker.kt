package com.vtol.petpal.presentation.explore.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.map.PlaceAddress
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun PlaceMarker(state: MarkerState, place: PlaceAddress) {
    MarkerComposable(
        state = state,
        anchor = Offset(0.5f, 1f) // anchors at bottom center like a pin
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MainPurple, RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = place.name,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            // pin triangle tip
            Canvas(modifier = Modifier.size(8.dp, 6.dp)) {
                drawPath(
                    path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width / 2, size.height)
                        close()
                    },
                    color = MainPurple
                )
            }
        }
    }
}