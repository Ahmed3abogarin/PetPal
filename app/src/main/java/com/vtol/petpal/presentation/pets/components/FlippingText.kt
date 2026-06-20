package com.vtol.petpal.presentation.pets.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.TextPurple
import kotlinx.coroutines.delay

@Composable
fun FlippingText(
    modifier: Modifier = Modifier,
    texts: List<String>,
    specieFontWeight: FontWeight = FontWeight.Medium,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textColor: Color = MainPurple,
    interval: Long = 2000L
) {
    if (texts.isEmpty()) return

    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(interval)
            index = (index + 1) % texts.size
        }
    }

    AnimatedContent(
        targetState = texts[index],
        transitionSpec = {
            (slideInVertically { it } + fadeIn()) togetherWith
                    (slideOutVertically { -it } + fadeOut())
        },
        label = "text_switch"
    ) { text ->
        val isSpecie = text.startsWith("Specie: ")

        Row(modifier = modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {

            if (isSpecie) {
                Text(
                    text = "Specie:",
                    fontSize = 13.sp,
                    fontWeight = specieFontWeight,
                    color = TextPurple
                )

                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = text.removePrefix("Specie: "),
                    fontSize = 13.sp,
                    fontWeight = fontWeight,
                    color = MainPurple
                )
            } else {
                Text(
                    text = text,
                    fontSize = 13.sp,
                    fontWeight = fontWeight,
                    color = textColor
                )
            }
        }
    }
}