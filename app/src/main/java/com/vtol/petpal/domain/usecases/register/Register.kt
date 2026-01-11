package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class Register(
    private val repository: AuthRepository
){

    suspend operator fun invoke(email: String, password: String): Result<Unit>{
        return repository.register(email,password)
    }

}
