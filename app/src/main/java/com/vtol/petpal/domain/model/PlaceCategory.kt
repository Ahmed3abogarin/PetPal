package com.vtol.petpal.domain.model

enum class PlaceCategory(val apiType: String, val displayName: String) {
    VETS("veterinary_care","Vets"),
    PET_STORES("pet_store", "Pet Stores"),
    PHARMACIES("pharmacy", "Pharmacies"),
    PARKS("park", "Parks")
}