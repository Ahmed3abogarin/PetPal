package com.vtol.petpal.presentation.pets.tabs.gallery

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.PetPhoto
import com.vtol.petpal.domain.usecases.gallery.DeleteGalleryImageUseCase
import com.vtol.petpal.domain.usecases.gallery.GetPhotosUseCase
import com.vtol.petpal.domain.usecases.gallery.UploadGalleryImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// presentation/gallery/GalleryViewModel.kt
data class GalleryUiState(
    val photos: List<PetPhoto> = emptyList(),
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val selectedPhoto: PetPhoto? = null,
    val error: String? = null
)

sealed class GalleryEvent {
    data class PhotoPicked(val uri: Uri) : GalleryEvent()
    data class DeletePhoto(val photo: PetPhoto) : GalleryEvent()
    data class SelectPhoto(val photo: PetPhoto?) : GalleryEvent()
}

sealed class GalleryEffect {
    data class ShowSnackbar(val message: String) : GalleryEffect()
}

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getPetPhotosUseCase: GetPhotosUseCase,
    private val addPetPhotoUseCase: UploadGalleryImageUseCase,
    private val deletePetPhotoUseCase: DeleteGalleryImageUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _state = MutableStateFlow(GalleryUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<GalleryEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadPhotos()
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.PhotoPicked -> uploadPhoto(event.uri)
            is GalleryEvent.DeletePhoto -> deletePhoto(event.photo)
            is GalleryEvent.SelectPhoto -> _state.update { it.copy(selectedPhoto = event.photo) }
        }
    }

    private fun loadPhotos() {
        getPetPhotosUseCase(petId)
            .onEach { photos ->
                _state.update { it.copy(photos = photos, isLoading = false) }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true) }
            addPetPhotoUseCase(petId, uri)
                .onSuccess {
                    _state.update { it.copy(isUploading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isUploading = false) }
                    _effect.send(GalleryEffect.ShowSnackbar("Failed to upload: ${e.message}"))
                }
        }
    }

    private fun deletePhoto(photo: PetPhoto) {
        viewModelScope.launch {
            deletePetPhotoUseCase(photo)
                .onFailure { e ->
                    _effect.send(GalleryEffect.ShowSnackbar("Failed to delete: ${e.message}"))
                }
        }
    }
}