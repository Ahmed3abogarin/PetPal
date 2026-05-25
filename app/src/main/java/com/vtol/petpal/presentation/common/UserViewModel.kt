package com.vtol.petpal.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(UserUiState())
    val state = _state.asStateFlow()


    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = appUseCases.getUser()) {
                is Resource.Success -> _state.update {
                    it.copy(
                        user = result.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> _state.update {
                    it.copy(
                        error = result.message,
                        isLoading = false
                    )
                }

                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class UserUiState(
    val isLoading: Boolean = false,
    val isImageUploading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)