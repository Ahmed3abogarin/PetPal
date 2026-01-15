package com.vtol.petpal.domain.model.map

data class VetAddress(
    val name: String,
    val lat: Double,
    val lng: Double,
    val address: String?
)