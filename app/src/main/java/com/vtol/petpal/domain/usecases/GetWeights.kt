package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetWeights(
    private val appRepository: AppRepository
) {
    operator fun invoke(petId: String): Flow<List<WeightRecord>>{
        return appRepository.getWeightList(petId)
    }
}