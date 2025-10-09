package com.vtol.petpal.domain.model

enum class PetGender {
    MALE, FEMALE
}

enum class WeightUnit(val displayName: String) {
    G("g"),
    KG("kg"),
    LBS("lbs")
}