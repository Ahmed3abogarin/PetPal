package com.vtol.petpal.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val appUseCases: AppUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(PetsState())
    val state = _state.asStateFlow()


    init {
        observePets()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePets() {
        appUseCases.getPets()
            .flatMapLatest { pets ->

                if (pets.isEmpty()) {
                    return@flatMapLatest flowOf(
                        PetsState(
                            pets = emptyList(),
                            firstTasks = emptyMap(),
                            isLoading = false
                        )
                    )
                }

                val taskFlows = pets.map { pet ->
                    appUseCases.getTasksById(pet.id)
                        .map { tasks ->
                            val now = System.currentTimeMillis()

                            pet.id to tasks
                                .filter { !it.isCompleted && it.dateTime > now }
                                .minByOrNull { it.dateTime }
                        }
                }

                combine(taskFlows) { pairs ->
                    PetsState(
                        pets = pets,
                        firstTasks = pairs.toMap(),
                        isLoading = false
                    )
                }
            }
            .onStart { emit(PetsState(isLoading = true)) }
            .catch { emit(PetsState(error = it.message)) }
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }
}


data class PetsState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val firstTasks: Map<String, TaskUi?> = emptyMap(),
    val error: String? = null
)