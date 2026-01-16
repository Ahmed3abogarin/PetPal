package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> =
        repository.isCompleted()

}