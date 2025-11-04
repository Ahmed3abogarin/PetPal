package com.vtol.petpal.domain.model

import com.vtol.petpal.R


enum class PetGender(val genderTxt: String, val icon: Int) {
    Male("Male", icon = R.drawable.ic_male), Female("Female", icon = R.drawable.ic_female), Unknown("Unknown",R.drawable.ic_unknown)
}

enum class WeightUnit(val displayName: String) {
    G("g"),
    KG("kg"),
    LBS("lbs")
}