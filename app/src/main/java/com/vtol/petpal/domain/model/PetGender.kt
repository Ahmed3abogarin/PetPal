package com.vtol.petpal.domain.model

enum class PetGender {
    Male, Female, Unknown
}

enum class WeightUnit(val displayName: String) {
    G("g"),
    KG("kg"),
    LBS("lbs")
}