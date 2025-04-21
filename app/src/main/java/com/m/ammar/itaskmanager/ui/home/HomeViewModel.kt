package com.m.ammar.itaskmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.enums.FilterOption
import com.m.ammar.itaskmanager.data.enums.SortOption
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


    private val _response = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Initial)
    val response: StateFlow<HomeScreenUiState> = _response.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            try {
                val tasks =
                    taskDao.getTasksSortedByPriority()
                _allTasks.value = tasks
                if (tasks.isNotEmpty()) _response.value = HomeScreenUiState.Success(tasks = tasks)
                else _response.value = HomeScreenUiState.Empty

            } catch (e: Exception) {
                _response.value = HomeScreenUiState.Error(
                    msg = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
            loadData()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            loadData()
        }
    }

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

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

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

    fun selectTask(task: Task) {
        _selectedTask.value = task
    }

}
