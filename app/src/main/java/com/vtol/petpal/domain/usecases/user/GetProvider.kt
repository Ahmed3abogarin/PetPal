package com.vtol.petpal.domain.usecases.user

import com.vtol.petpal.domain.repository.UserRepository
import javax.inject.Inject

class GetProvider @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() =
        repository.getProvider()
}