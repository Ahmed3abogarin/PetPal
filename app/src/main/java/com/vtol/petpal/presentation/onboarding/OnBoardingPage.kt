package com.vtol.petpal.presentation.onboarding

import androidx.annotation.DrawableRes
import com.vtol.petpal.R

data class OnBoardingPage (
    val title : String,
    val description: String,
    @DrawableRes val img: Int
)

val onBoardingPages = listOf(
    OnBoardingPage(
        title = "Easily care for your pet’s\n daily needs",
        description = "Track feeding, health, and activities in one place",
        img = R.drawable.onboarding_1
    ),
    OnBoardingPage(
        title = "Never miss a vet visit or feeding time",
        description = "Set reminders and keep all your pet’s records at your fingertips",
        img = R.drawable.onboarding_3
    ),
    OnBoardingPage(
        title = "Enjoy every moment with your pet",
        description = "From health tracking to daily care, everything your pet needs is here",
        img = R.drawable.onboarding_2
    )
)