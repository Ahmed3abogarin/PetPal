package com.vtol.petpal.presentation.navgraph.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import kotlinx.coroutines.delay

@Composable
fun AppBottomNavComponent(
    bottomItems: MutableList<BottomNavItem>,
    selectedIndex: Int,
    onItemClicked: (Int) -> Unit,
) {
    val switching = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(switching.value) {
        if (switching.value) {
            delay(250)
            switching.value = false
        }
    }


    // The row that contains all the bottom icons
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomItems.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                val iconOffset by animateDpAsState(
                    targetValue = if (isSelected) 6.dp else 0.dp, // how much it moves down
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 1f,
                    animationSpec = tween(300, easing = LinearOutSlowInEasing),
                    label = ""
                )
                val interactionSource = remember { MutableInteractionSource() }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable (
                        indication = null,
                        interactionSource =  interactionSource
                    ){
                        onItemClicked(index)
                    }
                ) {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(tween(300)),
                        exit = fadeOut(tween(300))
                    ) {
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .width(25.dp)
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(MainPurple)
                        )
                    }


                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (isSelected) LightPurple else Color.Gray,
                        modifier = Modifier
                            .size(36.dp)
                            .offset(y = iconOffset)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                    )
                    Text(
                        text = item.title,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }

}

data class BottomNavItem(
    val icon: ImageVector,
    val color: Color,
    val title: String,
    val offset: Offset = Offset(0f, 0f),
    val size: IntSize = IntSize(10, 10),
)
