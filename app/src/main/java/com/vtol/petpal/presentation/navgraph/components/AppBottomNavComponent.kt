package com.vtol.petpal.presentation.navgraph.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun AppBottomNavComponent(
    bottomItems: MutableList<BottomNavItem>,
    selectedIndex: Int,
    onItemClicked: (Int) -> Unit,
) {

    // The row that contains all the bottom icons
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .navigationBarsPadding()


    ) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.7.dp,
            color = ExtraLightPurple
        )

        Box {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp)
            ) {
                val itemWidth = maxWidth / bottomItems.size
                val pillWidth = 48.dp


                val targetOffset = itemWidth * selectedIndex + (itemWidth - pillWidth) / 2

                val animatedOffset by animateDpAsState(
                    targetValue = targetOffset,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                    label = "sliderOffset"
                )


                Column(
                    modifier = Modifier

                ) {
                    // The top Slider
                    Box(
                        modifier = Modifier
                            .offset(animatedOffset)
                            .height(4.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .width(pillWidth)
                            .background(MainPurple)

                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        bottomItems.forEachIndexed { index, item ->
                            val isSelected = index == selectedIndex

                            val interactionSource = remember { MutableInteractionSource() }


                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = interactionSource
                                    ) {
                                        onItemClicked(index)
                                    }
                                    .padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {

                                Image(
                                    painter = painterResource(if (isSelected) item.filledIcon else item.defaultIcon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(26.dp)
                                )
                                Text(
                                    text = item.title,
                                    color = if (isSelected) MainPurple else ExtraLightPurple,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun BottomNavPreview() {
    PetPalTheme {

        AppBottomNavComponent(
            bottomItems =
                mutableListOf(
                    BottomNavItem(
                        defaultIcon = R.drawable.ic_home_outlined,
                        filledIcon = R.drawable.ic_home_filled,
                        title = "Home"
                    ),
                    BottomNavItem(
                        defaultIcon = R.drawable.ic_pets_outlined,
                        filledIcon = R.drawable.ic_pets_filled,
                        title = "Pets"
                    ),
                    BottomNavItem(
                        defaultIcon = R.drawable.ic_calendar_outlined,
                        filledIcon = R.drawable.ic_calendar_filled,
                        title = "Calendar"
                    ),
                    BottomNavItem(
                        defaultIcon = R.drawable.ic_world_outlined,
                        filledIcon = R.drawable.ic_world_filled,
                        title = "Explore"
                    ),
                    BottomNavItem(
                        defaultIcon = R.drawable.ic_user_outlined,
                        filledIcon = R.drawable.ic_user_filled,
                        title = "Profile"
                    )
                ),
            selectedIndex = 0,
            onItemClicked = {

            }
        )
    }
}

data class BottomNavItem(
    val defaultIcon: Int,
    val filledIcon: Int,
    val title: String,
)
