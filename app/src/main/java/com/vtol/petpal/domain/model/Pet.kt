package com.vtol.petpal.domain.model

import com.google.firebase.Timestamp

data class Pet(
    val id: String = "",
    val petName: String = "",
    val imagePath: String = "",
    val birthDate: Long? = null,
    val gender: PetGender = PetGender.Unknown,
    val specie: String? = "",
    val weight: Double = 0.0,
    val weightUnit: WeightUnit = WeightUnit.G
)
