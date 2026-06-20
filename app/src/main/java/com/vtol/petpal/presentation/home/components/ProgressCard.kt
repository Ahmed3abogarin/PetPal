package com.vtol.petpal.presentation.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R

// ── Colors ────────────────────────────────────────────────────────────────────
private val CardBg = Color(0xFFFFFFFF)
private val PurplePrimary = Color(0xFF7B5CF5)   // vivid purple (progress fill)
private val PurpleLight = Color(0xFFD8D0FA)   // light lavender (track)
private val PurpleBadgeBg = Color(0xFFF0EEFF)   // badge pill background
private val TextPrimary = Color(0xFF111111)

// ── Main Composable ───────────────────────────────────────────────────────────
@Composable
fun ProgressCard(
    completed: Int,
    total: Int,
    progress: Float,
    percentage: Int
) {
    // Animate progress bar on first composition
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "progress"
    )
    LaunchedEffect(Unit) { animationPlayed = true }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 26.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // ── Row 1: Title + Badge ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Today's Progress",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                )

                BadgePill(label = "${completed}/${total} done")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Row 2: Motivational text + Dog emoji ───────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Keep going - you're doing great!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                // Sparkle dog area — rendered as emoji + sparkles
                Image(
                    modifier = Modifier.width(38.dp),
                    painter = painterResource(R.drawable.img_progress),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Progress Bar ───────────────────────────────────────────
            GradientProgressBar(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Row 3: "X completed" + "Y%" ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$completed completed",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = PurplePrimary
                )
                Text(
                    text = "$percentage%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PurplePrimary
                )
            }
        }
    }
}

// ── Badge pill ────────────────────────────────────────────────────────────────
@Composable
private fun BadgePill(label: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(PurpleBadgeBg)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = PurplePrimary
        )
    }
}

// ── Custom gradient progress bar ──────────────────────────────────────────────
@Composable
private fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(PurpleLight)   // track
    ) {
        // Filled portion
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PurplePrimary, Color(0xFF9C7CF8))
                    )
                )
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFFF0EFFF)
@Composable
fun ProgressCardPreview() {
    ProgressCard(
        total = 5,
        completed = 3,
        progress = 0.5f,
        percentage = 3
    )
}