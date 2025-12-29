package com.vtol.petpal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vtol.petpal.domain.model.tasks.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)


//    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM pet_tasks ORDER BY dateTime ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM pet_tasks WHERE petId = :petId ORDER BY dateTime ASC")
    fun getTask(petId: String): Flow<List<Task>>

    //  @Query("SELECT * FROM pet_tasks WHERE petId = :petId ORDER BY dateTime ASC")
}