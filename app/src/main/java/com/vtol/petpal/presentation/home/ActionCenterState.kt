package com.vtol.petpal.presentation.home

import com.vtol.petpal.domain.model.tasks.TaskUi

data class ActionCenterState(
    val overdueTasks: List<TaskUi> = emptyList(),
    val upcomingTasks: List<TaskUi> = emptyList()
)