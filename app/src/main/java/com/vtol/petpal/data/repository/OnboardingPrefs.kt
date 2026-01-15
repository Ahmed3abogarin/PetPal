package com.vtol.petpal.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey

object OnboardingPrefs {
    val COMPLETED = booleanPreferencesKey("onboarding_completed")
}