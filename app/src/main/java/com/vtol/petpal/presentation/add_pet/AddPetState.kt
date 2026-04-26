package com.vtol.petpal.presentation.add_pet

import android.net.Uri
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit

data class AddPetState(
    val petImage: Uri? = null,
    val petName: String = "",
    val petNameError: String? = null,
    val petBreed: String = "",
    val petSpecie: String = "",
    val petBreedError: String? = null,
    val petWeight: String = "",
    val petWeightError: String? = null,
    val petWeightUnit: WeightUnit = WeightUnit.LBS,
    val petGender: PetGender = PetGender.Unknown,
    val petGenderError: String? = null,
    val petBirthDate: Long? = null,


    val isLoading: Boolean = false,
    val error: String? = null
)
