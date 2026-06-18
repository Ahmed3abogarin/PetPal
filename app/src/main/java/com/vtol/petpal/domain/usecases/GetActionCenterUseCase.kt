package com.vtol.petpal.domain.usecases

import com.vtol.petpal.domain.repository.TaskRepository
import com.vtol.petpal.presentation.home.ActionCenterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetActionCenterUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<ActionCenterState> {

        return combine(
            repository.getOverdueTasks(),
            repository.getUpcomingTasks()
        ) { overdue, upcoming ->

            ActionCenterState(
                overdueTasks = overdue,
                upcomingTasks = upcoming
            )
        }
    }
}