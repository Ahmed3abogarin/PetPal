package com.vtol.petpal.domain.model.map

import com.vtol.petpal.R

enum class PlaceCategory(val apiType: String, val displayName: String, val image: Int) {
    VETS("veterinary_care","Vets", R.drawable.ic_vets),
    PET_STORES("pet_store", "Pet Stores", R.drawable.ic_pet_cart),
    PHARMACIES("pharmacy", "Pharmacies", R.drawable.ic_pharmacy),
    PARKS("park", "Parks", R.drawable.ic_parks)
}