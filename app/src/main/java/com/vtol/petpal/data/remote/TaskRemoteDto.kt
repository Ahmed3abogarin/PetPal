package com.vtol.petpal.data.remote

import com.vtol.petpal.domain.model.tasks.Task

data class TaskRemoteDto(
    val id: Long = 0,
    val petId: String = "",
    val title: String = "",
    val note: String? = null,
    val type: String = "",
    val dateTime: Long = 0L,
    val deletedDates: List<String> = emptyList(),
    val isCompleted: Boolean = false,
    val repeatInterval: String? = null,
    val details: String? = null
    // syncStatus omitted — not stored in Firestore
)

fun Task.toRemoteDto() = TaskRemoteDto(
    id = id,
    petId = petId,
    title = title,
    note = note,
    type = type.name,
    dateTime = dateTime,
    deletedDates = deletedDates.map { it.toString() },
    isCompleted = isCompleted,
    repeatInterval = repeatInterval?.name,
    details = details
)