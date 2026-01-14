package com.vtol.petpal.domain.usecases.register

import com.vtol.petpal.domain.repository.AuthRepository

class Logout(
    private val repository: AuthRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}