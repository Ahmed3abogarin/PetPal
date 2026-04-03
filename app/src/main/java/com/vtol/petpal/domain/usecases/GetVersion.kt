package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.model.AppVersion
import com.vtol.petpal.domain.repository.UpdateRepository

class GetVersion(
    private val updateRepository: UpdateRepository
) {

    suspend operator fun invoke(): AppVersion {
        return updateRepository.getVersion()
    }
}