package com.vtol.petpal.presentation.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.cardColors
import com.vtol.petpal.util.rememberShimmerBrush

@Composable
fun PlaceCardShimmer(modifier: Modifier = Modifier) {
    val headerBrush = rememberShimmerBrush(cardColors)
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3){

            Column(modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(Color.White).padding(12.dp)) {
                Row {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(78.dp)
                            .background(headerBrush)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.padding(top = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(8.dp)
                                .width(160.dp)
                                .background(headerBrush)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(6.dp)
                                .width(100.dp)
                                .background(headerBrush)
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(6.dp)
                                .width(60.dp)
                                .background(headerBrush)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .height(25.dp)
                            .weight(1f)
                            .background(headerBrush)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .height(25.dp)
                            .weight(1f)
                            .background(headerBrush)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .height(25.dp)
                            .weight(1f)
                            .background(headerBrush)
                    )

                }


            }

        }
    }

}


@Preview
@Composable
fun ShimmerPreview() {
    PetPalTheme {
        PlaceCardShimmer()
    }
}