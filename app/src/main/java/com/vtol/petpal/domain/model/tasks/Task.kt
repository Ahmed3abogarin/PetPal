package com.vtol.petpal.domain.model.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "pet_tasks")
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val title: String,
    val note: String?,
    val type: TaskType,
    val dateTime: Long,
    val deletedDates: List<LocalDate> = emptyList(),
    val isCompleted: Boolean = false,
    val repeatInterval: RepeatInterval? = null,
    val details: String? = null,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class RepeatInterval {
    Never, Daily, Weekly, Monthly
}