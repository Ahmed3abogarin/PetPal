package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.presentation.register.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun logout()
    suspend fun authState(): Flow<AuthState>

    // After the user register successfully save info in a collection
    fun createUserInfo(user: User)
}