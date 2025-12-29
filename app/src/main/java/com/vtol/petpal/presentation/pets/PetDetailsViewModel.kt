package com.vtol.petpal.presentation.pets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _petState = MutableStateFlow<Resource<Pet?>>(Resource.Loading)
    val petState = _petState.asStateFlow()

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()


    init {
        getPet(petId)
        getTask()
    }


    fun getPet(id: String){
        viewModelScope.launch {
            _petState.value = appUseCases.getPet(id)
        }
    }

    fun getTask(){
        viewModelScope.launch {

            _state.value = DetailsState(isLoading = true)
            try {
                appUseCases.getTasksById(petId).collect { tasks ->
                    _state.value = DetailsState(
                        nextTask = tasks.sortedBy { it.dateTime }[0],
                        isLoading = false
                    )
                }

            }catch (e: Exception) {
                _state.value = DetailsState(
                    isLoading = false,
                    error = e.message
                )
            }

        }

    }

}

data class DetailsState(
    val nextTask: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)