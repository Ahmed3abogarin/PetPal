package com.vtol.petpal.presentation.premium

import android.app.Activity

sealed class PremiumEvent {
    data class PlanSelected(val plan: PremiumPlan) : PremiumEvent()
    data class PurchaseClicked(val activity: Activity) : PremiumEvent()
    object LogScreenView : PremiumEvent()
}