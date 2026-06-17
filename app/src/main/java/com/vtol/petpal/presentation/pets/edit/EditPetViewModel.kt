package com.vtol.petpal.presentation.pets.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.pets.ValidatePetInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPetViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val validateInput: ValidatePetInputUseCase,
    private val analyticsHelper: FirebaseAnalyticsHelper,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private var originalPet: Pet? = null


    private val _state= MutableStateFlow(EditPetUiState())
    val uiState = _state.asStateFlow()

    private val _uiEffect = Channel<EditUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()


    init {
        loadPet()
    }


    fun onEvent(event: EditPetEvent) {
        when (event) {
            is EditPetEvent.ReloadPet -> loadPet()
            is EditPetEvent.OnNameChanged -> _state.update { it.copy(petName = event.name) }
            is EditPetEvent.OnBreedChanged -> _state.update { it.copy(breed = event.breed) }
            is EditPetEvent.OnGenderChanged -> _state.update { it.copy(gender = event.gender) }
            is EditPetEvent.OnImageChanged -> _state.update { it.copy(imageUri = event.uri) }
            is EditPetEvent.OnBirthDateChanged -> _state.update { it.copy(birthDate = event.birthDate) }
            is EditPetEvent.OnSpecieChanged -> _state.update {
                it.copy(specie = event.specie)
            }

            is EditPetEvent.OnPersonalityChanged -> _state.update {
                val current = uiState.value.personality
                val updated = if (event.personality in current) {
                    current - event.personality
                } else {
                    current + event.personality
                }
                it.copy(personality = updated)

            }

            is EditPetEvent.OnRemoveClicked -> _state.update { it.copy(imageUri = null, imagePath = "") }
            is EditPetEvent.OnSaveClicked -> updatePet()

            is EditPetEvent.LogScreenView -> analyticsHelper.logScreenView("edit_pet_screen")
        }
    }

    private fun updatePet() {

        val currentState = uiState.value

        val nameResult = validateInput.validateName(currentState.petName)
        val specieResult = validateInput.validateSpecie(currentState.specie)

        val hasError = listOf(
            nameResult,
            specieResult
        ).any { !it.successful }

        if (hasError) {
            _state.update {
                it.copy(
                    petNameError = nameResult.errorMessage,
                    petSpecieError = specieResult.errorMessage
                )
            }
            return
        }

        val existingPet = originalPet ?: return

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            val updatedPet = existingPet.copy(
                petName = currentState.petName,
                breed = currentState.breed,
                specie = currentState.specie,
                personality = currentState.personality,
                gender = currentState.gender,
                birthDate = currentState.birthDate
            )

            val result = appUseCases.updatePet(
                pet = updatedPet,
                uri = currentState.imageUri
            )

            result
                .onSuccess {
                    _uiEffect.send(EditUiEffect.NavigateUp)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            error = error.message
                        )
                    }
                    _uiEffect.send(EditUiEffect.ShowToastMessage(error.message ?: "Something went wrong"))
                    _uiEffect.send(EditUiEffect.NavigateUp)
                }
        }
    }

    private fun loadPet() {
        viewModelScope.launch {
            appUseCases.getPet(petId)
                .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
                .collect { pet ->

                    originalPet = pet

                    _state.update {
                        it.copy(
                            petId = pet.id,
                            petName = pet.petName,
                            imagePath = pet.imagePath,
                            birthDate = pet.birthDate,
                            gender = pet.gender,
                            breed = pet.breed,
                            specie = pet.specie,
                            personality = pet.personality,
                            isLoading = false
                        )
                    }
                }
        }
    }
}

data class EditPetUiState(
    val petId: String = "",
    val imagePath: String = "",
    val imageUri: Uri? = null,
    val petName: String = "",
    val petNameError: String? = null,
    val breed: String = "",
    val specie: String = "",
    val personality: List<String> = emptyList(),
    val petSpecieError: String? = null,
    val gender: PetGender = PetGender.Unknown,
    val petGenderError: String? = null,
    val birthDate: Long? = null,


    val isLoading: Boolean = false,
    val error: String? = null
)