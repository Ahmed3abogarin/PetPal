package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Resource

class GetPet(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(id: String): Resource<Pet?> {
        return appRepository.getPet(id)
    }
}