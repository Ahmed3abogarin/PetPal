package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.rememberShimmerBrush

@Composable
fun PetShimmerEffect(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = modifier
                .background(petPalGradient)
                .statusBarsPadding()
                .padding(16.dp)
                .padding(bottom = 28.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppIconButton {
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(brush),
                    )
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(brush),
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .size(122.dp)
                    .clip(CircleShape)
                    .background(brush),
            )


            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(brush)
                    .height(12.dp)
                    .width(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(brush)
                    .height(12.dp)
                    .width(180.dp)
            )


        }

        Box(modifier = Modifier
            .offset(y = (-22).dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(brush)
            .fillMaxSize()
        )
    }

}


@Preview
@Composable
fun PetShimmerPreview() {
    PetShimmerEffect()
}