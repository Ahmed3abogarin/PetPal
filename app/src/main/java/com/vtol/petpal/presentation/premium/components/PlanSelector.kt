package com.vtol.petpal.presentation.premium.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.premium.PremiumPlan

@Composable
fun PlanSelector(
    selectedPlan: PremiumPlan,
    onPlanSelected: (PremiumPlan) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PlanCard(
            title = "Monthly",
            price = "$1.99 / month",
            badge = null,
            isSelected = selectedPlan == PremiumPlan.MONTHLY,
            onClick = { onPlanSelected(PremiumPlan.MONTHLY) }
        )
        PlanCard(
            title = "Yearly",
            price = "$29.99 / year",
            badge = "Save 50%",
            isSelected = selectedPlan == PremiumPlan.YEARLY,
            onClick = { onPlanSelected(PremiumPlan.YEARLY) }
        )
    }
}