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

    private val _state = MutableStateFlow<Resource<Unit>?>(null)
    val state = _state.asStateFlow()


    fun addPet(pet: Pet){
        viewModelScope.launch {
            _state.value =  appUseCases.addPet(pet)
        }
    }
}