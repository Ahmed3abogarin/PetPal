package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.AuthRepository

class Register(
    private val repository: AuthRepository
){

    suspend operator fun invoke(user: User, password: String): Result<Unit>{
        return repository.register(user,password)
    }

}
