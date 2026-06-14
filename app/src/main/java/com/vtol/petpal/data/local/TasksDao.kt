package com.vtol.petpal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE pet_tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Query("DELETE FROM pet_tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("SELECT * FROM pet_tasks ORDER BY dateTime ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM pet_tasks WHERE petId = :petId ORDER BY dateTime ASC")
    fun getPetTasks(petId: String): Flow<List<Task>>

    @Query("SELECT * FROM pet_tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: String): Task?
    @Query("SELECT * FROM pet_tasks WHERE type = :type ORDER BY dateTime ASC")
    fun getSpecificTasks(type: TaskType): Flow<List<Task>>

    @Query("SELECT * FROM pet_tasks WHERE syncStatus IN ('PENDING', 'MODIFIED')")
    suspend fun getPendingSyncTasks(): List<Task>

    @Query("UPDATE pet_tasks SET syncStatus = :status WHERE id = :taskId")
    suspend fun updateSyncStatus(taskId: String, status: String)

    @Upsert
    suspend fun upsertRemoteTasks(tasks: List<Task>)
    //  @Query("SELECT * FROM pet_tasks WHERE petId = :petId ORDER BY dateTime ASC")
}