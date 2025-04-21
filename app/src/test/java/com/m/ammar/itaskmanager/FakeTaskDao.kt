package com.m.ammar.itaskmanager

import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.local.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.asStateFlow

class FakeTaskDao : TaskDao {

    private val tasks = mutableListOf<Task>()
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    override suspend fun insertTask(task: Task) {
        tasks.add(task)
        updateFlow()
    }

    override suspend fun deleteTask(task: Task) {
        tasks.removeIf { it.id == task.id }
        updateFlow()
    }

    override suspend fun getTasksSortedByPriority(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksById(id: Int): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByStatus(status: Boolean): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksSortedByDueDate(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            updateFlow()
        }
    }

    override suspend fun getTasksSortedAlphabetically(): List<Task> {
        return tasks.sortedBy { it.title.lowercase() }
    }

    override fun getAllTasksFlow(): Flow<List<Task>> = tasksFlow

    private fun updateFlow() {
        tasksFlow.value = tasks.toList()
    }
}
