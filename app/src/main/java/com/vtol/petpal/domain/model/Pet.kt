package com.vtol.petpal.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Pet(
    val id: String = "",
    val petName: String = "",
    val imagePath: String = "",
    val birthDate: Long? = null,
    val gender: PetGender = PetGender.Unknown,
    val breed: String? = "",
    val weight: List<WeightRecord> = emptyList(),
    val weightUnit: WeightUnit = WeightUnit.G
)

data class WeightRecord(
    @ServerTimestamp
    val date: Date? = null, // System.CurrentTimeMillis()
    val weight: Double = 0.0
)
