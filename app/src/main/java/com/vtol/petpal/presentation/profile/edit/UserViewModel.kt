package com.vtol.petpal.presentation.profile.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.UpdateUserImageUseCase
import com.vtol.petpal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val updateUserImageUseCase: UpdateUserImageUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserUiState())
    val state = _state.asStateFlow()


    init {
        getUser()
    }


    fun onEvent(event: EditEvents){
        when(event){
            is EditEvents.RemoveImage -> removeImage()
            is EditEvents.UpdateImage -> updateUserImage(event.uri)
        }
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

    private fun removeImage() {

    }


    private fun updateUserImage(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isImageUploading = true) }


            val result = updateUserImageUseCase(uri)

            result
                .onSuccess { imagePath ->
                    Timber.d(">>> onSuccess called")
                    _state.update {
                        it.copy(
                            isImageUploading = false,
                            user = it.user?.copy(imgPath = imagePath)
                        )
                    }
                }
                .onFailure { e ->
                    Timber.e(">>> onFailure called: ${e.message}")
                    _state.update { it.copy(isImageUploading = false, error = e.message) }
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