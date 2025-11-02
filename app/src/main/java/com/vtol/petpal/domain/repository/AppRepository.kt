package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.util.Resource

interface AppRepository {
    suspend fun addPet(pet: Pet): Resource<Unit>
}