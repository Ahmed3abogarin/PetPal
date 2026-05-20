package com.vtol.petpal.domain.model.tasks

import com.vtol.petpal.domain.model.tasks.details.TaskDetails

data class TaskUi(
    val id: Long,
    val petId: String,
    val title: String,
    val note: String?,
    val type: TaskType,
    val dateTime: Long,
    val isCompleted: Boolean,
    val repeatInterval: RepeatInterval?,
    val details: TaskDetails?,
    val syncStatus: SyncStatus
)