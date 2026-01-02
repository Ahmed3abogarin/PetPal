package com.vtol.petpal.presentation.pets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

//    private val _petState = MutableStateFlow<Resource<Pet?>>(Resource.Loading)
//    val petState = _petState.asStateFlow()

    private val _state = MutableStateFlow(DetailsState(isLoading = true))
    val state = _state.asStateFlow()


    init {
        observePetInfo()
    }

    fun addWeight(petId: String, weightRecord: WeightRecord){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                appUseCases.addWeight(petId, weightRecord)
                _state.update { it.copy(isLoading = false) }
            }catch (e: Exception){
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }


    fun observePetInfo() {

        viewModelScope.launch {
            try {

                val pet = appUseCases.getPet(petId)

                combine(
                    appUseCases.getTasksById(petId),
                    appUseCases.getWeights(petId)
                ) { tasks, weights ->
                    DetailsState(
                        pet = pet,
                        nextTask = if (tasks.isNotEmpty()) tasks.sortedBy { it.dateTime }[0] else null,
                        lastWeight = weights
                    )
                }
                    .catch { e ->
                        _state.update {
                            it.copy(
                                isLoading = false, error = e.message
                            )
                        }
                    }
                    .collect { newState -> _state.value = newState }


            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false, error = e.message
                    )
                }
            }

        }


    }

//    fun getTask() {
//        viewModelScope.launch {
//
//            _state.value = DetailsState(isLoading = true)
//            try {
//                appUseCases.getTasksById(petId).collect { tasks ->
//                    _state.value = DetailsState(
//                        nextTask = tasks.sortedBy { it.dateTime }[0],
//                        isLoading = false
//                    )
//                }
//
//            } catch (e: Exception) {
//                _state.value = DetailsState(
//                    isLoading = false,
//                    error = e.message
//                )
//            }
//
//        }
//
//    }

}

data class DetailsState(
    val nextTask: Task? = null,
    val pet: Pet? = null,

    // This will work in both OverView and Health tabs, since it in overview will just take the last sorted one, which the last weight the user updated so it represent the current pet's weight :)
    val lastWeight: List<WeightRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)