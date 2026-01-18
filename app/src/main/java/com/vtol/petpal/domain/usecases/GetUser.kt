package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.util.Resource

class GetUser(
    private val repository: UserRepository
){
    suspend operator fun invoke(): Resource<User> {
        return repository.getUser()
    }

}