package com.m.ammar.itaskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.m.ammar.itaskmanager.data.local.model.Task


@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY priority")
    suspend fun getTasksSortedByPriority(): List<Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = :status")
    suspend fun getTasksByStatus(status: Boolean): List<Task>

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    suspend fun getTasksSortedByDueDate(): List<Task>

    @Query("SELECT * FROM tasks ORDER BY title")
    suspend fun getTasksSortedAlphabetically(): List<Task>
}
