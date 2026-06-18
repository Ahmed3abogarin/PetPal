package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActionCenterCountUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getActionCenterCount()
    }
}