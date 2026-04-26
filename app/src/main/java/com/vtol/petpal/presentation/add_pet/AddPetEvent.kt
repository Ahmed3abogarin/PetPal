package com.vtol.petpal.presentation.add_pet

import android.net.Uri
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit

sealed class AddPetEvent {
    class OnNameChanged(val name: String): AddPetEvent()
    class OnImageChanged(val uri: Uri?): AddPetEvent()
    class OnWeightChanged(val weight: String): AddPetEvent()
    class OnWeightUnitChanged(val weightUnit: WeightUnit): AddPetEvent()
    class OnBreedChanged(val breed: String): AddPetEvent()
    class OnSpecieChanged(val specie: String): AddPetEvent()
    class OnBirthDateChanged(val birthDate: Long?): AddPetEvent()
    class OnGenderChanged(val gender: PetGender): AddPetEvent()
    object OnSaveClicked: AddPetEvent()
}