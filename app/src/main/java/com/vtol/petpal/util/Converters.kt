package com.vtol.petpal.util

import androidx.room.TypeConverter
import com.vtol.petpal.domain.model.tasks.TaskType
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromTaskType(value: TaskType) = value.name
    @TypeConverter
    fun toTaskType(value: String) = enumValueOf<TaskType>(value)

    @TypeConverter
    fun fromLocalDateList(dates: List<LocalDate>?): String? {
        // Converts [2026-06-13, 2026-06-14] -> "2026-06-13,2026-06-14"
        return dates?.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun toLocalDateList(datesString: String?): List<LocalDate> {
        if (datesString.isNullOrEmpty()) return emptyList()
        // Converts "2026-06-13,2026-06-14" -> [2026-06-13, 2026-06-14]
        return datesString.split(",").map { LocalDate.parse(it) }
    }
}