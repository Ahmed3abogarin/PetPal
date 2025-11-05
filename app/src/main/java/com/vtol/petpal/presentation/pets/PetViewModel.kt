package com.vtol.petpal.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val appUseCases: AppUseCases
): ViewModel() {

    private val _addPetState = MutableStateFlow<Resource<Unit>?>(null)
    val addPetState = _addPetState.asStateFlow()

    private val _state = MutableStateFlow<Resource<List<Pet>>?>(Resource.Loading)
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

    fun getPets(){
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = appUseCases.getPets()
        }
    }
}