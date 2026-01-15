package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class SaveAppEntry(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(){
        repository.setCompleted()
    }
}