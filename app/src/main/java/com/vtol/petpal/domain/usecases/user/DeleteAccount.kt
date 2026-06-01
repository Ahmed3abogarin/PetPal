package com.vtol.petpal.domain.usecases.user

import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.presentation.profile.edit.ReAuthCredential
import javax.inject.Inject

class DeleteAccount @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(credential: ReAuthCredential) =
        repository.deleteAccount(credential)
}