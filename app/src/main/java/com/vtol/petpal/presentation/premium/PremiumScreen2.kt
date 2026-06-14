package com.vtol.petpal.presentation.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PremiumScreen3(
    onUpgrade: (PlanType) -> Unit,
    onRestore: () -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedPlan by remember { mutableStateOf(PlanType.YEARLY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7FF))
            .verticalScroll(rememberScrollState())
    ) {
        // Hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6C47FF))
                .padding(horizontal = 20.dp)
                .padding(top = 28.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Upgrade to Premium",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = "Unlock everything PetPal has to offer for your pets",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Plan selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlanType.entries.forEach { plan ->
                    PlanCard(
                        plan = plan,
                        isSelected = selectedPlan == plan,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedPlan = plan }
                    )
                }
            }

            // Features
            FeaturesCard()

            // CTA
            Button(
                onClick = { onUpgrade(selectedPlan) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C47FF))
            ) {
                Text(
                    text = "Start free 7-day trial",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            TextButton(
                onClick = onRestore,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Restore purchases",
                    fontSize = 12.sp,
                    color = Color(0xFFB8B0D8)
                )
            }

            Text(
                text = "Cancel anytime · Auto-renews · Terms apply",
                fontSize = 10.sp,
                color = Color(0xFFC8C0E0),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }
    }
}

enum class PlanType(
    val label: String,
    val price: String,
    val period: String,
    val saving: String?,
    val badge: String?,
) {
    MONTHLY("Monthly", "$4.99", "/mo", null, null),
    YEARLY("Yearly", "$2.99", "/mo", "Save 40%", "Best value"),
}

@Composable
fun PlanCard(
    plan: PlanType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) Color(0xFF6C47FF) else Color(0xFFE5E3F5),
                    shape = RoundedCornerShape(14.dp)
                )
                .background(if (isSelected) Color(0xFFF4F1FF) else Color.White)
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Text(plan.label, fontSize = 11.sp, color = Color(0xFF9990C0), fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(plan.price, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A2E))
                Text(plan.period, fontSize = 11.sp, color = Color(0xFF9990C0), modifier = Modifier.padding(bottom = 2.dp))
            }
            Text(
                text = plan.saving ?: "",
                fontSize = 10.sp,
                color = Color(0xFF6C47FF)
            )
        }

        plan.badge?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-9).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6C47FF))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(it, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun FeaturesCard() {
    val features = listOf(
        Triple(Icons.Outlined.Pets, Color(0xFF6C47FF), Color(0xFFF0ECFF)) to Pair("Unlimited pets", "Add as many pets as you need, no limits"),
        Triple(Icons.Outlined.Notifications, Color(0xFF22C55E), Color(0xFFF0FFF5)) to Pair("Smart reminders", "Medication, feeding, and vet visit alerts"),
        Triple(Icons.Outlined.BarChart, Color(0xFFF59E0B), Color(0xFFFFFBF0)) to Pair("Health insights", "Track trends and view detailed reports"),
        Triple(Icons.Outlined.Cloud, Color(0xFF3B82F6), Color(0xFFF0F9FF)) to Pair("Cloud backup", "Your data synced and secured automatically"),
        Triple(Icons.Outlined.Phone, Color(0xFFEC4899), Color(0xFFFFF0F5)) to Pair("Priority support", "Get help faster with dedicated support"),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(0.5.dp, Color(0xFFE5E3F5), RoundedCornerShape(14.dp))
            .background(Color.White)
    ) {
        Text(
            text = "WHAT YOU GET",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF9990C0),
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = Color(0xFFF0EEFA), thickness = 0.5.dp)

        features.forEach { (iconData, text) ->
            val (icon, tint, bg) = iconData
            val (title, sub) = text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A2E))
                    Text(sub, fontSize = 11.sp, color = Color(0xFF9990C0), lineHeight = 15.sp, modifier = Modifier.padding(top = 1.dp))
                }
            }
            if (features.last().second != text) {
                HorizontalDivider(color = Color(0xFFF5F3FC), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 14.dp))
            }
        }
    }
}


@Preview
@Composable
fun PremiumPreview2(){
    PetPalTheme {
        PremiumScreen3(
            onDismiss = {},
            onRestore = {},
            onUpgrade = {}
        )
    }
}