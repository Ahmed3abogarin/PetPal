package com.vtol.petpal.domain.usecases.gallery

import android.net.Uri
import com.vtol.petpal.domain.repository.GalleryRepository
import com.vtol.petpal.domain.util.ImageCompressor
import javax.inject.Inject

class UploadGalleryImageUseCase @Inject constructor(
    private val repository: GalleryRepository,
    private val compressor: ImageCompressor
) {
    suspend operator fun invoke(petId: String, uri: Uri): Result<Unit> {
        val image = compressor.compress(uri)
        return repository.uploadPhoto(petId, image)
    }
}