package com.vtol.petpal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vtol.petpal.Converters
import com.vtol.petpal.domain.model.tasks.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class TasksDB: RoomDatabase() {
    abstract val tasksDao: TasksDao
}