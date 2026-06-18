package com.vtol.petpal.data.repository

import com.google.gson.Gson
import com.vtol.petpal.data.local.TasksDao
import com.vtol.petpal.data.mapper.toUiModel
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TasksDao,
    private val gson: Gson
): TaskRepository {
    override fun getOverdueTasks(): Flow<List<TaskUi>> {
        return dao.getOverdueTasks()
            .map { list -> list.map { it.toUiModel(gson) } }
    }

    override fun getUpcomingTasks(
        nextHours: Int
    ): Flow<List<TaskUi>> {

        val now = System.currentTimeMillis()

        val endTime =
            now + TimeUnit.HOURS.toMillis(nextHours.toLong())

        return dao.getUpcomingTasks(
            now,
            endTime
        ).map { list ->
            list.map { it.toUiModel(gson) }
        }
    }

    override fun getPendingTasks(): Flow<List<TaskUi>> {
        return dao.getPendingTasks()
            .map { list -> list.map { it.toUiModel(gson) } }
    }

    override fun getActionCenterCount(): Flow<Int> {
        val now = System.currentTimeMillis()
        val next24Hours = now + TimeUnit.HOURS.toMillis(24)

        return dao.getActionCenterCount(now = now, next24Hours = next24Hours)
    }
}