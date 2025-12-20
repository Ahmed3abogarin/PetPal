package com.vtol.petpal

import androidx.room.TypeConverter
import com.vtol.petpal.domain.model.tasks.TaskType

class Converters {
    @TypeConverter
    fun fromTaskType(value: TaskType) = value.name
    @TypeConverter
    fun toTaskType(value: String) = enumValueOf<TaskType>(value)
}