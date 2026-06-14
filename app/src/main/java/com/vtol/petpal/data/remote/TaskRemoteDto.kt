package com.vtol.petpal.data.remote

import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import java.time.LocalDate
import java.util.UUID

data class TaskRemoteDto(
    val id: String = UUID.randomUUID().toString(),
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

fun TaskRemoteDto.toDomain() = Task(
    id = id,
    petId = petId,
    title = title,
    note = note,
    type = TaskType.valueOf(type),
    dateTime = dateTime,
    deletedDates = deletedDates.map { LocalDate.parse(it) },
    isCompleted = isCompleted,
    repeatInterval = repeatInterval?.let { RepeatInterval.valueOf(it) },
    details = details,
    syncStatus = SyncStatus.SYNCED  // always SYNCED when coming from cloud
)