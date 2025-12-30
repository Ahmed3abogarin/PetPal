package com.vtol.petpal.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
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


    init {
        observePets()
    }

    // TODO: CREATE THE STATE CLASS FOR THE PETS

    fun addPet(pet: Pet, weight: WeightRecord){
        viewModelScope.launch {
            _addPetState.value = Resource.Loading
            _addPetState.value =  appUseCases.addPet(pet,weight)

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

    private fun observePets() {
        appUseCases.getPets() // The infinite stream from Firestore
            .map { pets ->
                // Whenever pets change, fetch their tasks concurrently
                val tasksMap = coroutineScope {
                    pets.map { pet ->
                        async {
                            pet.id to appUseCases.getTasksById(pet.id).firstOrNull()?.firstOrNull()
                        }
                    }.awaitAll().toMap()
                }
                // Return combined result
                PetsState(pets = pets, firstTasks = tasksMap, isLoading = false)
            }
            .onStart { emit(PetsState(isLoading = true)) }
            .catch { emit(PetsState(error = it.message)) }
            .onEach { _state.value = it } // Update UI
            .launchIn(viewModelScope)
    }


}




data class PetsState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val firstTasks: Map<String, Task?> = emptyMap(),
    val error: String? = null
)