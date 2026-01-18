package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.util.Resource

interface UserRepository {
    suspend fun getUser(): Resource<User>
}