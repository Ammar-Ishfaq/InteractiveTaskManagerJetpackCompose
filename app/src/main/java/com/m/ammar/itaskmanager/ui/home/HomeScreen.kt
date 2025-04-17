package com.m.ammar.itaskmanager.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m.ammar.itaskmanager.data.enums.FilterOption
import com.m.ammar.itaskmanager.R
import com.m.ammar.itaskmanager.data.enums.SortOption
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.components.ErrorItem
import com.m.ammar.itaskmanager.ui.components.AppLoader
import com.m.ammar.itaskmanager.utility.toReadableDate
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    loadData: () -> Unit,
    onCreateNewTask: () -> Unit,
    onTaskClick: (taskId: Int) -> Unit // Navigate to task detail screen
) {
    LaunchedEffect(Unit) {
        loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        floatingActionButton = {
            val haptic = LocalHapticFeedback.current
            FloatingActionButton(onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCreateNewTask()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Create Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {

                is HomeScreenUiState.Loading -> {
                    AppLoader(modifier = Modifier.fillMaxSize())
                }

                is HomeScreenUiState.Success -> {

                    HomeScreenContent(
                        tasks = uiState.tasks, // List of tasks
                        onTaskClick = onTaskClick
                    )
                }

                is HomeScreenUiState.Error -> {
                    ErrorItem(
                        text = uiState.msg,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                HomeScreenUiState.Initial -> {}

            }
        }
    }
}


@Composable
private fun HomeScreenContent(
    tasks: List<Task>, // Task data passed from UIState
    onTaskClick: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks.size) { index ->
            TaskItem(task = tasks[index], onClick = { onTaskClick(tasks[index].id) })

        }
    }
}
@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            task.description?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Due: ${task.dueDate.toReadableDate()}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun TaskFilterSortBar(
    selectedSort: SortOption,
    selectedFilter: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit
) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Sort:")
            SortOption.values().forEach {
                FilterChip(
                    selected = it == selectedSort,
                    onClick = { onSortChange(it) },
                    label = { Text(it.name.lowercase().replaceFirstChar { c -> c.uppercase() }) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Filter:")
            FilterOption.values().forEach {
                FilterChip(
                    selected = it == selectedFilter,
                    onClick = { onFilterChange(it) },
                    label = { Text(it.name.lowercase().replaceFirstChar { c -> c.uppercase() }) }
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    // Convert date string to milliseconds (Long)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dueDate1 = dateFormat.parse("2025-04-10")?.time ?: 0L
    val dueDate2 = dateFormat.parse("2025-04-15")?.time ?: 0L

    HomeScreen(
        loadData = {},
        uiState = HomeScreenUiState.Success(
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Task 1",
                    description = "This is the description of task 1",
                    priority = "High",
                    dueDate = dueDate1, // dueDate as Long (milliseconds)
                    isCompleted = false
                ),
                Task(
                    id = 2,
                    title = "Task 2",
                    description = "This is the description of task 2",
                    priority = "Medium",
                    dueDate = dueDate2, // dueDate as Long (milliseconds)
                    isCompleted = true
                )
            )
        ),
        onCreateNewTask = {},
        onTaskClick = {}
    )
}


