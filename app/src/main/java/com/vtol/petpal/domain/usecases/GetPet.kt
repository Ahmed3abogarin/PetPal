package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetPet(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(id: String): Flow<Pet> {
        return appRepository.getPet(id)
    }
}