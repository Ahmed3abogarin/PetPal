package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.util.Resource

interface UserRepository {
    suspend fun getUser(): Resource<User>

    suspend fun updateUserProfileImage(bytes: ByteArray): Result<String>
    suspend fun updateUsername(name: String): Result<Unit>
    suspend fun updatePhoneNumber(phone: String): Result<Unit>
    suspend fun updatePassword(oldPw: String,newPw: String): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>
}