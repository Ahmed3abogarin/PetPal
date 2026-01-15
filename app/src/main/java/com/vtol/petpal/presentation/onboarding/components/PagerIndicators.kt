package com.vtol.petpal.presentation.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PagerIndicators(
    modifier: Modifier = Modifier,
    pageCount: Int = 3,
    currentPage: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            // indicator color
            val color by animateColorAsState(
                targetValue = if (isSelected) MainPurple else LightPurple,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )


            // the horizontal indicator
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .weight(1f)
                    .clip(
                        shape = CircleShape
                    )
                    .background(color)
            )
        }
    }
}

@Preview
@Composable
fun PagerPreview() {
    PetPalTheme {
        PagerIndicators(currentPage = 1)
    }
}