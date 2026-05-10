package com.vtol.petpal.presentation.onboarding

import androidx.annotation.DrawableRes
import com.vtol.petpal.R

data class OnBoardingPage (
    val titleBold : String,
    val titleLight : String,
    val description: String,
    @param:DrawableRes val img: Int
)

val onBoardingPages = listOf(
    OnBoardingPage(
        titleBold = "Pet Care",
        titleLight = "Companion",
        description = "Keep your furry friend healthy, happy, and loved with everything you need in one simple place.",
        img = R.drawable.onboarding_1
    ),
    OnBoardingPage(
        titleBold = "Daily pet care",
        titleLight = "made easy",
        description = "Track routines, manage appointments, set reminders, and stay organized without the stress.",
        img = R.drawable.onboarding_2
    ),
    OnBoardingPage(
        titleBold = "Every pet",
        titleLight = "deserves great care",
        description = "Start your journey with PetPal and make every moment with your companion more meaningful.",
        img = R.drawable.onboarding_3
    )

)