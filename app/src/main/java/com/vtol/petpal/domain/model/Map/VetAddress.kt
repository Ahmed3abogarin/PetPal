package com.vtol.petpal.domain.model.Map

data class VetAddress(
    val name: String,
    val lat: Double,
    val lng: Double,
    val address: String?
)