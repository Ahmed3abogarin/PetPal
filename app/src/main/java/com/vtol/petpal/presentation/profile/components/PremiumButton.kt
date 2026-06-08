package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun PremiumButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = listOf(
        Gold,
        DarkGold,
        Color(0xFFFFC53E)
    )

    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(width = 2.dp, brush = Brush.sweepGradient(colors))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier= Modifier.weight(1f),
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
                        color = MainPurple,
                        text = buildAnnotatedString {
                            append("PetPal")
                            withStyle(style = SpanStyle(color = Gold)) {
                                append("Premium")
                            }
                        }
                    )

                    Text(
                        text = "Upgrade now to unlock special features, priority support, and unlimited pets.",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                }

            }
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = Color.Black,
                contentDescription = ""
            )
        }
    }

}

@Preview
@Composable
fun PremiumPreview() {
    PetPalTheme {
        PremiumButton {}
    }
}