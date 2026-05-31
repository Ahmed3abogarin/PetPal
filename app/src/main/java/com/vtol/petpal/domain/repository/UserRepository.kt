package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>

    suspend fun updateUserProfileImage(bytes: ByteArray): Result<String>
    suspend fun updateUsername(name: String): Result<Unit>
    suspend fun updatePhoneNumber(phone: String): Result<Unit>
    suspend fun updatePassword(oldPw: String,newPw: String): Result<Unit>

    suspend fun deleteUserImage(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}