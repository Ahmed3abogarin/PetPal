package com.vtol.petpal.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appUseCases: AppUseCases
): ViewModel() {

    private val _state = MutableStateFlow<Resource<User>>(Resource.Loading)
    val state = _state.asStateFlow()


    init {
        getUser()
    }
    private fun getUser(){
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = appUseCases.getUser()
        }
    }
}