package com.m.ammar.itaskmanager.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.local.db.AppDatabase
import com.m.ammar.itaskmanager.data.local.dao.TaskDao
import com.m.ammar.itaskmanager.data.local.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            _response.value = HomeScreenUiState.Loading
            try {
                val tasks =
                    taskDao.getTasksSortedByPriority() // You can add logic to sort/filter differently
                _response.value = HomeScreenUiState.Success(tasks = tasks)
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
            loadData() // Reload list after adding
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
            loadData() // Reload list after deletion
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
            loadData() // Reload list after update
        }
    }


//    var sortOption by mutableStateOf(SortOption.PRIORITY)
//    var filterOption by mutableStateOf(FilterOption.ALL)
//
//    val tasksFlow = repo.taskDao.getAllTasksFlow().map { tasks ->
//        val filtered = when (filterOption) {
//            FilterOption.ALL -> tasks
//            FilterOption.COMPLETED -> tasks.filter { it.isCompleted }
//            FilterOption.PENDING -> tasks.filter { !it.isCompleted }
//        }
//
//        when (sortOption) {
//            SortOption.PRIORITY -> filtered.sortedBy { it.priority }
//            SortOption.DUE_DATE -> filtered.sortedBy { it.dueDate }
//            SortOption.ALPHABETICAL -> filtered.sortedBy { it.title }
//        }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}
