package com.vtol.petpal.presentation.pets.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPetViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])


    private val _state= MutableStateFlow(EditPetUiState())
    val uiState = _state.asStateFlow()

    private fun loadPet() {
        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            val pet = appUseCases.getPet(petId)

            _state.update {
                it.copy(
                    isLoading = false,
                    petId = pet.id,
                    petName = pet.petName,
                    imagePath = pet.imagePath,
                    birthDate = pet.birthDate,
                    gender = pet.gender,
                    breed = pet.breed,
                    specie = pet.specie,
                    personality = pet.personality
                )
            }
        }
    }
}

data class EditPetUiState(
    val petId: String = "",
    val imagePath: String = "",
    val petName: String = "",
    val petNameError: String? = null,
    val breed: String = "",
    val specie: String = "",
    val personality: List<String> = emptyList(),
    val petSpecieError: String? = null,
    val petWeight: String = "",
    val petWeightError: String? = null,
    val petWeightUnit: WeightUnit = WeightUnit.LBS,
    val gender: PetGender = PetGender.Unknown,
    val petGenderError: String? = null,
    val birthDate: Long? = null,


    val isLoading: Boolean = false,
    val error: String? = null
)