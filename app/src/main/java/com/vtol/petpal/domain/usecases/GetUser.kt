package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.util.Resource
import kotlinx.coroutines.flow.Flow

class GetUser(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User> {
        return repository.getUser()
    }
}