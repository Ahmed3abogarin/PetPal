package com.vtol.petpal.domain.model

import androidx.annotation.Keep

@Keep
data class Pet(
    val id: String = "",
    val petName: String = "",
    val imagePath: String = "",
    val birthDate: Long? = null,
    val gender: PetGender = PetGender.Unknown,
    val breed: String? = "",
    val specie: String? = "",
)
@Keep
data class WeightRecord(
    val weight: Double = 0.0,
    val unit: WeightUnit = WeightUnit.G,
    val timestamp: Long = System.currentTimeMillis(),
)
