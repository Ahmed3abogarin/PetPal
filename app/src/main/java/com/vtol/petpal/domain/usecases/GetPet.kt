package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository

class GetPet(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(id: String): Pet {
        return appRepository.getPet(id)
    }
}