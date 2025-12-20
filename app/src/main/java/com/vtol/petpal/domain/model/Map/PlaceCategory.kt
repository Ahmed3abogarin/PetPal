package com.vtol.petpal.domain.model.Map

import com.vtol.petpal.R

enum class PlaceCategory(val apiType: String, val displayName: String, val image: Int) {
    VETS("veterinary_care","Vets", R.drawable.ic_vets),
    PET_STORES("pet_store", "Pet Stores", R.drawable.ic_stores),
    PHARMACIES("pharmacy", "Pharmacies", R.drawable.ic_pill),
    PARKS("park", "Parks", R.drawable.ic_walk)
}