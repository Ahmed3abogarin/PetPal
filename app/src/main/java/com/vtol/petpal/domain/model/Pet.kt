package com.vtol.petpal.domain.model

data class Pet(
    val id: Int,
    val petName: String,
    val imagePath: String,
    val birthDate: Long?,
    val gender: PetGender,
    val specie: String?,
    val weight: Double,
    val weightUnit: WeightUnit
)
