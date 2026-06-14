package com.vtol.petpal.util

import com.vtol.petpal.R
import com.vtol.petpal.domain.model.PetGender

object Constants {
    const val PETS_COLLECTION = "Pets"
    const val USERS_COLLECTION = "Users"
    const val WEIGHT_COLLECTION = "Weight"
    const val FEEDBACK_COLLECTION = "Feedback"

    const val TASKS_COLLECTION = "tasks"

    const val EMERGENCY_COLLECTION = "EmergencyContacts"
    const val MIN_REQUIRED_VERSION = "min_required_version"
    const val LATEST_VERSION = "latest_version"
    val species = listOf(
        Pair(R.drawable.ic_cat, "Cat"),
        Pair(R.drawable.ic_dog, "Dog"),
        Pair(R.drawable.ic_bird, "Bird"),
        Pair(R.drawable.ic_pets, "Other"),
    )

    val genders = listOf(
        Pair(R.drawable.ic_male, PetGender.Male),
        Pair(R.drawable.ic_female, PetGender.Female)
    )
}