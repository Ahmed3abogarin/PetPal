package com.vtol.petpal.domain.usecases.premium

import com.vtol.petpal.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsPremiumUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository
) {
    operator fun invoke(): Flow<Boolean> = premiumRepository.isPremium()
}