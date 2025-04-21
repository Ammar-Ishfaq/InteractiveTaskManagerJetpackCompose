package com.m.ammar.itaskmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.local.enums.FilterOption
import com.m.ammar.itaskmanager.data.local.enums.SortOption
import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.local.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskDao: TaskDao,
) : ViewModel() {

    /**
     * Loads all tasks from the database and updates the state.
     */
    fun loadData() {
        viewModelScope.launch {
            val tasks = taskDao.getTasksSortedAlphabetically()
            _allTasks.value = tasks
        }
    }

    /**
     * Adds a new task to the database and reloads the task list.
     *
     * @param task The task to be added.
     */
    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
            loadData()
        }
    }

    private var lastDeleteTask: ArrayList<Task?> = arrayListOf()

    /**
     * Deletes a task from the database and stores it for possible undo.
     *
     * @param task The task to be deleted.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            lastDeleteTask.add(task)
            taskDao.deleteTask(task)
            loadData()
        }
    }

    /**
     * Updates a task in the database and reloads the task list.
     *
     * @param task The task to be updated.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
            loadData()
        }
    }

    private val _sortOption = MutableStateFlow(SortOption.DUE_DATE)
    val sortOption = _sortOption.asStateFlow() // Now reactive

    private val _filterOption = MutableStateFlow(FilterOption.ALL)
    val filterOption = _filterOption.asStateFlow() // Now reactive

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())

    /**
     * Combines the task list with the selected sorting and filtering options to return a reactive flow.
     */
    val tasksFlow = combine(
        _allTasks,
        _sortOption,
        _filterOption
    ) { tasks, sort, filter ->
        tasks
            .applyFilter(filter)
            .applySort(sort)

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(50000),
        emptyList()
    )

    /**
     * Sets the sorting option for tasks.
     *
     * @param option The sort option to be set.
     */
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    /**
     * Sets the filter option for tasks.
     *
     * @param option The filter option to be set.
     */
    fun setFilterOption(option: FilterOption) {
        _filterOption.value = option
    }

    private fun List<Task>.applySort(sort: SortOption): List<Task> = when (sort) {
        SortOption.DUE_DATE -> sortedBy { it.dueDate }
        SortOption.PRIORITY -> sortedBy { it.priority.weight }
        SortOption.ALPHABETICAL -> sortedBy { it.title.lowercase() }
    }

    private fun List<Task>.applyFilter(filter: FilterOption): List<Task> = when (filter) {
        FilterOption.ALL -> this
        FilterOption.COMPLETED -> filter { it.isCompleted }
        FilterOption.PENDING -> filter { !it.isCompleted }
    }

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    /**
     * Sets the selected task.
     *
     * @param task The task to be selected.
     */
    fun selectTask(task: Task) {
        _selectedTask.value = task
    }

    /**
     * Marks a task as completed and reloads the task list.
     *
     * @param it The task to be completed.
     */
    fun completeTask(it: Task) {
        viewModelScope.launch {
            taskDao.updateTask(it.copy(isCompleted = true))
            loadData()
        }
    }

    /**
     * Restores the last deleted task.
     */
    fun undoDelete() {
        viewModelScope.launch {
            lastDeleteTask.lastOrNull()?.let {
                lastDeleteTask.remove(it)
                addTask(it)
            }
        }
    }

}
