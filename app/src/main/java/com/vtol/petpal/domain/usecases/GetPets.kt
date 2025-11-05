package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Resource

class GetPets(
    private val appRepository: AppRepository
){
    suspend operator fun invoke(): Resource<List<Pet>> {
        return appRepository.getPets()
    }

}