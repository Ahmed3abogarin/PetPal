package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskUi
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getOverdueTasks(): Flow<List<TaskUi>>

    fun getUpcomingTasks(
        nextHours: Int = 24
    ): Flow<List<TaskUi>>

    fun getPendingTasks(): Flow<List<TaskUi>>

    fun getActionCenterCount(): Flow<Int>
}