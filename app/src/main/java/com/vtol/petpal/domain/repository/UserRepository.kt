package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.user.ProviderInfo
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.presentation.profile.edit.ReAuthCredential
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>

    suspend fun updateUserProfileImage(bytes: ByteArray): Result<String>
    suspend fun updateUsername(name: String): Result<Unit>
    suspend fun updatePhoneNumber(phone: String): Result<Unit>
    suspend fun updatePassword(oldPw: String,newPw: String): Result<Unit>

    suspend fun deleteUserImage(): Result<Unit>
    suspend fun deleteAccount(credential: ReAuthCredential): Result<Unit>

    suspend fun getProvider(): ProviderInfo
}