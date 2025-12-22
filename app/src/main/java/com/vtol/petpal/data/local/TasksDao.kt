package com.vtol.petpal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.vtol.petpal.domain.model.tasks.Task

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)


//    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}