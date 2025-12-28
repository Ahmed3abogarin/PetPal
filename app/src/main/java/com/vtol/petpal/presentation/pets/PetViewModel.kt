package com.vtol.petpal.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val appUseCases: AppUseCases
): ViewModel() {

    private val _addPetState = MutableStateFlow<Resource<Unit>?>(null)
    val addPetState = _addPetState.asStateFlow()

    private val _state = MutableStateFlow(PetsState())
    val state = _state.asStateFlow()

    private val _petMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val petMap = _petMap.asStateFlow()

    init {
        getPets()
    }

    fun addPet(pet: Pet){
        viewModelScope.launch {
            _addPetState.value = Resource.Loading
            _addPetState.value =  appUseCases.addPet(pet)

        }
    }

    fun getPets(){
        appUseCases.getPets()
            .map { pets ->
                PetsState(
                    pets = pets,
                    isLoading = false
                )
            }
            .onStart {
                emit(PetsState(isLoading = true))
            }
            .catch { e ->
                emit(
                    PetsState(
                        isLoading = false,
                        error = e.message ?: "Failed to load pets"
                    )
                )
            }
            .onEach {
                _state.value = it
                _petMap.value = it.pets.associate {pet -> pet.id to pet.petName}
            }
            .launchIn(viewModelScope)
    }
}

data class PetsState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)