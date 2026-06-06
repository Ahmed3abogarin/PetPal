package com.vtol.petpal.presentation.pets.edit

import android.net.Uri
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit

sealed class EditPetEvent {
    class OnNameChanged(val name: String): EditPetEvent()
    class OnImageChanged(val uri: Uri?): EditPetEvent()
    class OnWeightChanged(val weight: String): EditPetEvent()
    class OnWeightUnitChanged(val weightUnit: WeightUnit): EditPetEvent()
    class OnBreedChanged(val breed: String): EditPetEvent()
    class OnSpecieChanged(val specie: String): EditPetEvent()
    class OnPersonalityChanged(val personality: String): EditPetEvent()
    class OnBirthDateChanged(val birthDate: Long?): EditPetEvent()
    class OnGenderChanged(val gender: PetGender): EditPetEvent()
    object OnRemoveClicked: EditPetEvent()
    object OnSaveClicked: EditPetEvent()
}