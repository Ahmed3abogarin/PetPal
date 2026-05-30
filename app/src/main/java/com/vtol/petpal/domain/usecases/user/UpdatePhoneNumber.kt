package com.vtol.petpal.domain.usecases.user

import com.vtol.petpal.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePhoneNumber @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(phone: String) =
        repository.updatePhoneNumber(phone)
}