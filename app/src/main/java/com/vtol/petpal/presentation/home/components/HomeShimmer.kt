package com.vtol.petpal.presentation.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun HomeShimmer() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    val headerBrush = Brush.linearGradient(
        colors = listOf(Color(0x26FFFFFF), Color(0x4DFFFFFF), Color(0x26FFFFFF)),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )
    val bodyBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFE0DAFA), Color(0xFFF0EEFF), Color(0xFFE0DAFA)),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )
    val cardBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFECECEC), Color(0xFFF8F8F8), Color(0xFFECECEC)),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF0EEFF))) {
        // Header shimmer

        Card(
            colors = CardDefaults.cardColors(containerColor = MainPurple),
            shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(32.dp).clip(CircleShape).background(headerBrush))
                        Box(Modifier.width(60.dp).height(14.dp).clip(RoundedCornerShape(6.dp)).background(headerBrush))
                    }
                    Box(Modifier.size(32.dp).clip(CircleShape).background(headerBrush))
                }
                Spacer(Modifier.height(16.dp))
                Box(Modifier.width(80.dp).height(13.dp).clip(RoundedCornerShape(6.dp)).background(headerBrush))
                Spacer(Modifier.height(6.dp))
                Box(Modifier.width(120.dp).height(20.dp).clip(RoundedCornerShape(6.dp)).background(headerBrush))
                Spacer(Modifier.height(8.dp))
            }
        }


        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Pet avatars shimmer
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(3) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.size(60.dp).clip(CircleShape).background(bodyBrush))
                        Box(Modifier.width(44.dp).height(11.dp).clip(RoundedCornerShape(4.dp)).background(bodyBrush))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Progress card shimmer
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(14.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(Modifier.width(130.dp).height(14.dp).clip(RoundedCornerShape(6.dp)).background(cardBrush))
                            Box(Modifier.width(90.dp).height(11.dp).clip(RoundedCornerShape(6.dp)).background(cardBrush))
                        }
                        Box(Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(cardBrush))
                    }
                    Spacer(modifier= Modifier.height(18.dp))
                    Box(Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(10.dp)).background(cardBrush))
                }
            }
        }

        Spacer(modifier= Modifier.height(14.dp))

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)){
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(2){
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(14.dp)).background(bodyBrush))
                }
            }
        }
    }
}

@Preview
@Composable
fun ShimmerPreview(){
    PetPalTheme {

        HomeShimmer()
    }
}