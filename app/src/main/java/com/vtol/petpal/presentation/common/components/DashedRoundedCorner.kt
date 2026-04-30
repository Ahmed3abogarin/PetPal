package com.vtol.petpal.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun DashedRoundedBorder(
    modifier: Modifier = Modifier,
    color: Color = MainPurple,
    strokeWidth: Float = 4f,
    cornerRadius: Dp = 14.dp,
    dashWidth: Float = 20f,
    gapWidth: Float = 10f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val radius = cornerRadius.toPx()
            drawRoundRect(
                color = color,
                size = size,
                cornerRadius = CornerRadius(radius, radius),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(dashWidth, gapWidth),
                        phase = 0f
                    )
                )
            )
        }
        content()
    }
}