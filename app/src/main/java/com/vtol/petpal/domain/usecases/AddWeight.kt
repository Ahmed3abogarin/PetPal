package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.repository.AppRepository

class AddWeight(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(petId: String, weight: WeightRecord) {
        appRepository.addWeight(petId = petId, weightRecord = weight)

    }
}