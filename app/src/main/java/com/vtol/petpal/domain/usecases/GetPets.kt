package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.repository.AppRepository
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

class GetPets(
    private val appRepository: AppRepository
){
    operator fun invoke(): Flow<List<Pet>> {
        return appRepository.getPets()
    }

}