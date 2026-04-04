package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.AppVersion

interface UpdateRepository {

    suspend fun getVersion(): AppVersion

}