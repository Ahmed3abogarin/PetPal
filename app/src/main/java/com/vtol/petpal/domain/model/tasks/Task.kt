package com.vtol.petpal.domain.model.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val petId: Long,
    val title: String,
    val description: String?,
    val type: TaskType,
    val dateTime: Long,
    val isCompleted: Boolean = false,
    val repeatInterval: RepeatInterval? = null,
    val details: String? = null
)

enum class RepeatInterval {
    NONE, DAILY, WEEKLY, MONTHLY
}