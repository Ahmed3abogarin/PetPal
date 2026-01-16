package com.vtol.petpal.domain.usecases

import android.net.Uri
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Resource

class AddPet(
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(pet: Pet,imageUri: Uri?, weight: WeightRecord): Resource<Unit> =
        appRepository.addPet(pet,imageUri,weight)

}