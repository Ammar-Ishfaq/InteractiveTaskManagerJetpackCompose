package com.m.ammar.itaskmanager.data.local.model

import android.opengl.Visibility
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: Priority,
    val dueDate: Long,
    var isCompleted: Boolean = false
)