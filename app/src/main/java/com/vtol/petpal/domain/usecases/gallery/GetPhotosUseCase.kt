package com.vtol.petpal.domain.usecases.gallery

import com.vtol.petpal.domain.repository.GalleryRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    operator fun invoke(petId: String) =
        repository.getPhotos(petId)
}