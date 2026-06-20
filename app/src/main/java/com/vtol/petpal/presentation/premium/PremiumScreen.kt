package com.vtol.petpal.presentation.premium

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.premium.premiumFeatures
import com.vtol.petpal.presentation.premium.components.PlanSelector
import com.vtol.petpal.presentation.premium.components.PremiumFeatureRow
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PremiumScreen(
    state: PremiumUiState,
    onEvent: (PremiumEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        onEvent(PremiumEvent.LogScreenView)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundColor,
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding() // Kept here to pad the top close button area
            // REMOVED: .navigationBarsPadding() moved from here
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // Close button
                IconButton(
                    onClick = navigateUp,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        tint = Color.Black
                    )
                }

                // Crown icon
                Image(
                    painter = painterResource(R.drawable.img_premium),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(184.dp)
                        .padding(top = 22.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth() // Changed fillMaxSize to fillMaxWidth for card stability
                    .shadow(2.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(22.dp))

                // Title
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_crown),
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.width(34.dp)
                    )
                    Text(
                        text = "Unlock PetPal Premium",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                Text(
                    text = "Everything your pet deserves",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )
                val period = when (state.selectedPlan) {
                    PremiumPlan.MONTHLY -> "7"
                    PremiumPlan.YEARLY -> "14"
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Features list
                premiumFeatures.forEach { feature ->
                    PremiumFeatureRow(feature = feature)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Plan selector
                PlanSelector(
                    selectedPlan = state.selectedPlan,
                    onPlanSelected = { onEvent(PremiumEvent.PlanSelected(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "\uD83C\uDF89 $period-Day Free Trial",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                // CTA Button
                Button(
                    onClick = {
                        activity?.let {
                            onEvent(PremiumEvent.PurchaseClicked(it))
                        }
                    },
                    enabled = !state.isPurchasing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainPurple
                    )
                ) {
                    if (state.isPurchasing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = when (state.selectedPlan) {
                                PremiumPlan.MONTHLY -> "Start Monthly – $4.99/mo"
                                PremiumPlan.YEARLY -> "Start Yearly – $19.9 9/yr"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Error
                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fine print
                Text(
                    text = "Cancel anytime • Secure payment via Google Play",
                    style = MaterialTheme.typography.bodySmall.copy(
                        // Quick note: dynamic black text here so it's readable on the white background card
                        color = Color.Black.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // ADDED: Navigation bar padding is safely placed inside the white card now
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(24.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun PremiumPreview() {
    PetPalTheme {
        PremiumScreen(
            state = PremiumUiState(),
            onEvent = {},
            navigateUp = {}
        )
    }
}