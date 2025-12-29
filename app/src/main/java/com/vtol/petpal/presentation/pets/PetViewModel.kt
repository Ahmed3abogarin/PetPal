package com.vtol.petpal.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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


    init {
        getPets()
    }

    fun addPet(pet: Pet){
        viewModelScope.launch {
            _addPetState.value = Resource.Loading
            _addPetState.value =  appUseCases.addPet(pet)

        }
    }

//    fun getPetsV2(){
//        appUseCases.getPets()
//            .map { pets ->
//                PetsState(
//                    pets = pets,
//                    isLoading = false
//                )
//            }
//            .onStart {
//                emit(PetsState(isLoading = true))
//            }
//            .catch { e ->
//                emit(
//                    PetsState(
//                        isLoading = false,
//                        error = e.message ?: "Failed to load pets"
//                    )
//                )
//            }
//            .onEach {
//                _state.value = it
//                appUseCases.getTasks(it.pets.forEach { it.id })
//                _petMap.value = it.pets.associate { pet -> pet.id to pet.petName }
//            }
//            .launchIn(viewModelScope)
//    }

    private fun getPets() {
        viewModelScope.launch {
            try {
                _state.value = PetsState(isLoading = true)

                // Fetch pets
                val pets = appUseCases.getPets().first() // or suspend function

                // Fetch first task for each pet concurrently
                val firstTasksMap = pets.associate { pet ->
                    val firstTask = appUseCases.getTasksById(pet.id).firstOrNull()
                    pet.id to firstTask
                }

                // Update state
                _state.value = PetsState(
                    pets = pets,
                    firstTasks = firstTasksMap,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = PetsState(
                    isLoading = false,
                    error = e.message ?: "Failed to load pets"
                )
            }
        }
    }
}




data class PetsState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val firstTasks: Map<String, List<Task>?> = emptyMap(),
    val error: String? = null
)