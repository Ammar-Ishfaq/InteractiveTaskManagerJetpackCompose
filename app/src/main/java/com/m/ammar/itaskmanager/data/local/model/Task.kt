package com.m.ammar.itaskmanager.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: Priority, // "Low", "Medium", or "High"
    val dueDate: Long, // Store as String (e.g., "2025-04-10")
    var isCompleted: Boolean = false
)