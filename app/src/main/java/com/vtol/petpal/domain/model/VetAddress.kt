package com.vtol.petpal.domain.model

data class VetAddress(
    val name: String,
    val lat: Double,
    val lng: Double,
    val address: String?
)
