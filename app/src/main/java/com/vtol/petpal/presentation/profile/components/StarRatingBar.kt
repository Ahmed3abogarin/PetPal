package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun StarRatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row (modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)){
        repeat(5) { index ->
            IconButton(
                onClick = { onRatingChanged(index + 1) }
            ) {
                Icon(
                    modifier = Modifier.size(46.dp),
                    imageVector = Icons.Outlined.StarBorder,
                    contentDescription = null,
                    tint = if (index < rating) MainPurple else ExtraLightPurple
                )
            }
        }
    }
}