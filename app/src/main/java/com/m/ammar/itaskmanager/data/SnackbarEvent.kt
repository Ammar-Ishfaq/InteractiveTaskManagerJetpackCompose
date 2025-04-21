package com.m.ammar.itaskmanager.data

import com.m.ammar.itaskmanager.data.local.model.Task

sealed class SnackbarEvent {
    data class TaskDeleted(
        val task: Task,
        val undo: () -> Unit  // Now properly defined
    ) : SnackbarEvent()

    data class TaskCompleted(
        val task: Task,
        val undo: () -> Unit  // Now properly defined
    ) : SnackbarEvent()
}