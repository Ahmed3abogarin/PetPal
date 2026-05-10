package com.vtol.petpal.domain.model

import androidx.annotation.Keep

@Keep
data class Pet(
    val id: String = "",
    val petName: String = "",
    val imagePath: String = "",
    val birthDate: Long? = null,
    val gender: PetGender = PetGender.Unknown,
    val breed: String = "",
    val specie: String = "",
    val personality: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
@Keep
data class WeightRecord(
    val weight: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
)
