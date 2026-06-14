package com.vtol.petpal.domain.repository

import kotlinx.coroutines.flow.Flow

interface PremiumRepository {
    fun isPremium(): Flow<Boolean>
}