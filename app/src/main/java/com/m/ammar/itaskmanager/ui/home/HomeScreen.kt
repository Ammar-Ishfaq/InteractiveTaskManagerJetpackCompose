package com.m.ammar.itaskmanager.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.ammar.itaskmanager.R
import com.m.ammar.itaskmanager.data.enums.FilterOption
import com.m.ammar.itaskmanager.data.enums.SortOption
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.components.AppLoader
import com.m.ammar.itaskmanager.ui.components.ErrorItem
import com.m.ammar.itaskmanager.utility.toReadableDate
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    tasks: List<Task>,
    sortOption: SortOption,
    filterOption: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit,
    onCreateNewTask: () -> Unit,
    onTaskClick: (taskId: Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Manage your day like a boss 💼",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
                is HomeScreenUiState.Empty -> {
                    EmptyTaskView()
                }

                is HomeScreenUiState.Success -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TaskFilterSortBar(
                            selectedSort = sortOption,
                            selectedFilter = filterOption,
                            onSortChange = onSortChange,
                            onFilterChange = onFilterChange
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HomeScreenContent(
                            tasks = tasks,
                            onTaskClick = onTaskClick
                        )
                    }
                }

                is HomeScreenUiState.Error -> {
                    ErrorItem(
                        text = uiState.msg,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                HomeScreenUiState.Initial -> Unit
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
fun EmptyTaskView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_task))
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(250.dp)
                    .padding(16.dp)
            )

            Text(
                text = "No tasks yet!",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Create a new task to get started.",
                style = MaterialTheme.typography.bodyMedium
            )
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
            Text(
                text = "Due: ${task.dueDate.toReadableDate()}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskFilterSortBar(
    selectedSort: SortOption,
    selectedFilter: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // SORT - DROPDOWN
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Sort by:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(8.dp))

            Box {
                Text(
                    text = selectedSort.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onSortChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // FILTER - CHIPS
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Filter:", style = MaterialTheme.typography.labelMedium)
            FilterOption.entries.forEach { option ->
                FilterChip(
                    selected = option == selectedFilter,
                    onClick = { onFilterChange(option) },
                    label = {
                        Text(option.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                )
            }
        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Sample tasks
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dueDate1 = dateFormat.parse("2025-04-10")?.time ?: 0L
    val dueDate2 = dateFormat.parse("2025-04-15")?.time ?: 0L

    val mockTasks = listOf(
        Task(
            id = 1,
            title = "Buy groceries",
            description = "Milk, Bread, Eggs, Butter",
            priority = Priority.LOW,
            dueDate = dueDate1,
            isCompleted = false
        ),
        Task(
            id = 2,
            title = "Doctor Appointment",
            description = "Visit Dr. Ali at 5 PM",
            priority = Priority.MEDIUM,
            dueDate = dueDate2,
            isCompleted = false
        )
    )

    HomeScreen(
        uiState = HomeScreenUiState.Success(tasks = mockTasks),
        tasks = mockTasks,
        sortOption = SortOption.DUE_DATE,
        filterOption = FilterOption.ALL,
        onSortChange = {},
        onFilterChange = {},
        onCreateNewTask = {},
        onTaskClick = {}
    )
}


