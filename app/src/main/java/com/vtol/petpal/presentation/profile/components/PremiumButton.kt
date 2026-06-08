package com.vtol.petpal.presentation.profile.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.DarkGold
import com.vtol.petpal.ui.theme.Gold
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import android.graphics.Matrix
import android.graphics.SweepGradient
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp

@Composable
fun PremiumButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = listOf(
        Gold,
        DarkGold,
        Color(0xFFFFC53E),
        Gold // Tip: Ending with the starting color prevents a sharp seam line in sweep gradients
    )

    val cardShape = RoundedCornerShape(16.dp)

    Card(
        modifier = modifier
            .glowingMovingBorder(
                colors = colors,
                borderWidth = 1.dp,
                shape = cardShape,
                durationMillis = 2500 // 2.5 seconds per rotation loop
            ),
        onClick = onClick,
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = "Crown icon"
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        fontWeight = FontWeight.Medium,
                        color = MainPurple,
                        text = buildAnnotatedString {
                            append("PetPal ")
                            withStyle(style = SpanStyle(color = Gold)) {
                                append("Premium")
                            }
                        }
                    )

                    Text(
                        text = "Upgrade now to unlock special features, priority support, and unlimited pets.",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = Color.DarkGray,
                contentDescription = ""
            )
        }
    }
}


fun Modifier.glowingMovingBorder(
    colors: List<Color>,
    borderWidth: Dp = 2.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    durationMillis: Int = 3000 // Speed of rotation
): Modifier = composed {
    // 1. Set up the infinite rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "glowing_border")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )

    this.drawWithContent {
        // 2. Draw the inner content of your Card first
        drawContent()

        // 3. Create a custom ShaderBrush that rotates with our animated angle
        val rotatingShaderBrush = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val shader = SweepGradient(
                    size.width / 2f,
                    size.height / 2f,
                    colors.map { it.toArgb() }.toIntArray(),
                    null
                )
                val matrix = Matrix()
                matrix.postRotate(angle, size.width / 2f, size.height / 2f)
                shader.setLocalMatrix(matrix)
                return shader
            }
        }

        // 4. Trace the shape outline and draw the glowing border strokes
        val outline = shape.createOutline(size, layoutDirection, this)

        // Optional: Draw a wider, semi-transparent background stroke to simulate a "glow aura"
        drawOutline(
            outline = outline,
            brush = rotatingShaderBrush,
            style = Stroke(width = borderWidth.toPx() * 2.5f),
            alpha = 0.35f
        )

        // Draw the main crisp border stroke
        drawOutline(
            outline = outline,
            brush = rotatingShaderBrush,
            style = Stroke(width = borderWidth.toPx())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumPreview() {
    PetPalTheme {
        PremiumButton {}
    }
}