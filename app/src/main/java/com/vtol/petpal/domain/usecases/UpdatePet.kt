package com.vtol.petpal.domain.usecases

import android.net.Uri
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.util.ImageCompressor

class UpdatePet(
    private val repository: AppRepository,
    private val imageCompressor: ImageCompressor
) {
    suspend operator fun invoke(pet: Pet, uri: Uri?): Result<Unit> {
        if (uri == null) {
            return repository.updatePet(pet, null)
        }
        val imageCompressed = imageCompressor.compress(uri)
        return repository.updatePet(pet, imageCompressed)
    }
}