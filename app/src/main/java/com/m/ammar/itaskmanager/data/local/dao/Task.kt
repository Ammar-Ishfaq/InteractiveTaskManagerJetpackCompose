package com.m.ammar.itaskmanager.data.local.dao

import androidx.room.*
import com.m.ammar.itaskmanager.data.local.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing task-related database operations.
 */
@Dao
interface TaskDao {

    /** Inserts a new task into the database. */
    @Insert
    suspend fun insertTask(task: Task)

    /** Updates an existing task in the database. */
    @Update
    suspend fun updateTask(task: Task)

    /** Deletes a task from the database. */
    @Delete
    suspend fun deleteTask(task: Task)

    /** Returns tasks sorted by priority (ascending). */
    @Query("SELECT * FROM tasks ORDER BY priority")
    suspend fun getTasksSortedByPriority(): List<Task>

    /** Returns a task by its unique ID. */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTasksById(id: Int): Task?

    /** Returns tasks filtered by completion status. */
    @Query("SELECT * FROM tasks WHERE isCompleted = :status")
    suspend fun getTasksByStatus(status: Boolean): List<Task>

    /** Returns tasks sorted by due date (ascending). */
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    suspend fun getTasksSortedByDueDate(): List<Task>

    /** Returns tasks sorted alphabetically by title. */
    @Query("SELECT * FROM tasks ORDER BY title")
    suspend fun getTasksSortedAlphabetically(): List<Task>

    /** Emits a flow of all tasks in the database. */
    @Query("SELECT * FROM tasks")
    fun getAllTasksFlow(): Flow<List<Task>>
}
