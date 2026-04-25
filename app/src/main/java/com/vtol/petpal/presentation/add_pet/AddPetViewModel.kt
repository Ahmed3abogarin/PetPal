package com.vtol.petpal.presentation.add_pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.pets.ValidatePetInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val useCases: AppUseCases,
    private val validateInput: ValidatePetInputUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddPetState())
    val state = _state.asStateFlow()

    private val _uiEffect = Channel<UiEffects>()
    val uiEffect = _uiEffect.receiveAsFlow()


    fun onEvent(event: AddPetEvent) {
        when (event) {
            is AddPetEvent.OnNameChanged -> _state.update { it.copy(petName = event.name) }
            is AddPetEvent.OnBreedChanged -> _state.update { it.copy(petBreed = event.breed) }
            is AddPetEvent.OnGenderChanged -> _state.update { it.copy(petGender = event.gender) }
            is AddPetEvent.OnWeightChanged -> _state.update { it.copy(petWeight = event.weight) }
            is AddPetEvent.OnWeightUnitChanged -> _state.update { it.copy(petWeightUnit = event.weightUnit) }
            is AddPetEvent.OnImageChanged -> _state.update { it.copy(petImage = event.uri) }
            is AddPetEvent.OnBirthDateChanged -> _state.update { it.copy(petBirthDate = event.birthDate) }
            is AddPetEvent.OnSaveClicked -> savePet()

        }
    }

    fun savePet() {
        val currentState = _state.value
        // Validate first
        val nameResult = validateInput.validateName(currentState.petName)
        val weightResult = validateInput.validateWeight(currentState.petWeight)
        val breedResult = validateInput.validateBreed(currentState.petBreed)

        val hasError = listOf(nameResult, weightResult, breedResult).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    petNameError = nameResult.errorMessage,
                    petWeightError = weightResult.errorMessage,
                    petBreedError = breedResult.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Create the pet instance
            try {
                val pet = Pet(
                    petName = currentState.petName,
                    gender = currentState.petGender,
                    birthDate = currentState.petBirthDate,
                    breed = currentState.petBreed

                )

                val weightRecord = WeightRecord(
                    weight = currentState.petWeight.toDouble(),
                    unit = currentState.petWeightUnit
                )

                useCases.addPet(pet = pet, imageUri = state.value.petImage, weight = weightRecord)

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _uiEffect.send(UiEffects.ShowToastMessage(e.message ?: "Something went wrong"))
            }
        }
    }
}