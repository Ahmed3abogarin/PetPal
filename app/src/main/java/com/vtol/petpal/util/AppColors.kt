package com.vtol.petpal.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {

    val headerGradientColors = listOf(Color(0XFF8638FE), Color(0XFFA266FF))

    val petPalGradient = Brush.linearGradient(headerGradientColors)

    // Shimmer effect colors
    val headerColors = listOf(Color(0x26FFFFFF), Color(0x4DFFFFFF), Color(0x26FFFFFF))
    val bodyColors = listOf(Color(0xFFE0DAFA), Color(0xFFF0EEFF), Color(0xFFE0DAFA))
    val cardColors = listOf(Color(0xFFECECEC), Color(0xFFF8F8F8), Color(0xFFECECEC))
}