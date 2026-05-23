package com.vtol.petpal.domain.usecases

import android.net.Uri
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.domain.util.ImageCompressor
import com.vtol.petpal.util.Resource

class AddPet(
    private val appRepository: AppRepository,
    private val imageCompressor: ImageCompressor
) {
    suspend operator fun invoke(pet: Pet, imageUri: Uri?, weight: WeightRecord): Resource<Unit> {
        if (imageUri == null) {
            return appRepository.addPet(pet, null, weight)
        }
        val image = imageCompressor.compress(imageUri)
        return appRepository.addPet(pet, image, weight)
    }
}