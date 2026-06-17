package com.vtol.petpal.domain.usecases.gallery

import com.vtol.petpal.domain.model.PetPhoto
import com.vtol.petpal.domain.repository.GalleryRepository
import javax.inject.Inject

class DeleteGalleryImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    suspend operator fun invoke(photo: PetPhoto) =
        repository.deletePhoto(photo)
}