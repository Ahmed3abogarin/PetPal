package com.vtol.petpal.domain.model.premium

import com.vtol.petpal.R
data class PremiumFeature(
    val icon: Int,
    val title: String,
    val description: String
)

val premiumFeatures = listOf(
    PremiumFeature(R.drawable.ic_pets, "Unlimited pets", "Add as many pets as you need, no limits"),
    PremiumFeature(R.drawable.ic_ai_chat, "AI Bot", "Get personalized pet care advice powered by AI"),
    PremiumFeature(R.drawable.ic_gallery, "Gallery", "Store unlimited photos and memories for your pets"),
    PremiumFeature(R.drawable.ic_back_up, "Cloud Sync", "Back up and restore your pet tasks across devices"),
)