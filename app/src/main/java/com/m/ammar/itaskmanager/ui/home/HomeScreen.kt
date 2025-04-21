package com.m.ammar.itaskmanager.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.m.ammar.itaskmanager.R
import com.m.ammar.itaskmanager.data.local.enums.FilterOption
import com.m.ammar.itaskmanager.data.local.enums.SortOption
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.components.BouncyFAB
import com.m.ammar.itaskmanager.ui.components.EmptyTaskView
import com.m.ammar.itaskmanager.ui.components.FilterSortActionButtons
import com.m.ammar.itaskmanager.ui.components.SwipeableTaskItem
import com.m.ammar.itaskmanager.ui.components.TaskStatsHeader
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Composable function that represents the home screen of the task manager app.
 * Displays tasks, sorting, filtering options, and floating action buttons for creating tasks and accessing settings.
 *
 * @param tasks List of tasks to be displayed on the screen.
 * @param sortOption The current sorting option selected for tasks.
 * @param filterOption The current filter option selected for tasks.
 * @param onSortChange Callback to handle changes in the sorting option.
 * @param onFilterChange Callback to handle changes in the filter option.
 * @param onCreateNewTask Callback to handle the creation of a new task.
 * @param onTaskClick Callback when a task is clicked.
 * @param onTaskCompleted Callback when a task is marked as completed.
 * @param onTaskDeleted Callback when a task is deleted.
 * @param onUndoCompleted Callback to undo the completion of a task.
 * @param onUndoDeleted Callback to undo the deletion of a task.
 * @param onSettingsClick Callback when the settings button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tasks: List<Task>,
    sortOption: SortOption,
    filterOption: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit,
    onCreateNewTask: () -> Unit,
    onTaskClick: (taskId: Task) -> Unit,
    onTaskCompleted: (task: Task) -> Unit,
    onTaskDeleted: (task: Task) -> Unit,
    onUndoCompleted: (task: Task) -> Unit,
    onUndoDeleted: (task: Task) -> Unit,
    onSettingsClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarVisible = snackbarHostState.currentSnackbarData != null
    val fabBottomOffset by animateDpAsState(
        targetValue = if (snackbarVisible) 96.dp else 16.dp,
        label = "FAB Offset Animation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                actions = {
                    FilterSortActionButtons(
                        sortOption = sortOption,
                        filterOption = filterOption,
                        onSortChange = onSortChange,
                        onFilterChange = onFilterChange
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (tasks.isEmpty()) {
                EmptyTaskView()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    visible = tasks.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    TaskStatsHeader(tasks = tasks)
                }
                HomeScreenContent(
                    tasks = tasks,
                    onTaskClick = onTaskClick,
                    onTaskCompleted = onTaskCompleted,
                    onTaskDeleted = onTaskDeleted,
                    snackbarHostState = snackbarHostState,
                    onUndoCompleted = onUndoCompleted,
                    onUndoDeleted = onUndoDeleted
                )


            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .zIndex(0f)
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = fabBottomOffset, end = 16.dp)
                    .zIndex(1f)
            ) {
                BouncyFAB(
                    onClick = { onSettingsClick() },
                    icon = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
                BouncyFAB(
                    onClick = { onCreateNewTask() },
                    icon = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    }

}


/**
 * Composable function that renders the content of the home screen including the list of tasks.
 * The tasks are displayed in a lazy column with swipeable actions for completing or deleting tasks.
 *
 * @param tasks List of tasks to be displayed.
 * @param onTaskClick Callback when a task is clicked.
 * @param onTaskCompleted Callback when a task is marked as completed.
 * @param onTaskDeleted Callback when a task is deleted.
 * @param snackbarHostState State for managing snackbars.
 * @param onUndoCompleted Callback to undo the completion of a task.
 * @param onUndoDeleted Callback to undo the deletion of a task.
 */
@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    onTaskClick: (task: Task) -> Unit,
    onTaskCompleted: (task: Task) -> Unit,
    onTaskDeleted: (task: Task) -> Unit,
    snackbarHostState: SnackbarHostState,
    onUndoCompleted: (task: Task) -> Unit,
    onUndoDeleted: (task: Task) -> Unit
) {
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
            val isInPreview = LocalInspectionMode.current
            val visible = remember { mutableStateOf(false) }
            if (!isInPreview) {
                LaunchedEffect(Unit) {
                    delay(index * 50L)
                    visible.value = true
                }
            }
            AnimatedVisibility(
                visible = (if (isInPreview) true else visible.value),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                SwipeableTaskItem(
                    scope = scope,
                    task = task,
                    snackbarHostState = snackbarHostState,
                    onDelete = onTaskDeleted,
                    onComplete = onTaskCompleted,
                    onUndoCompleted = onUndoCompleted,
                    onUndoDeleted = onUndoDeleted,
                    onClick = onTaskClick
                )

            }

        }

    }
}


/**
 * Preview composable function to display a mock version of the home screen.
 * This is used for inspecting how the UI looks with mock task data in the IDE.
 */
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
        tasks = mockTasks,
        sortOption = SortOption.DUE_DATE,
        filterOption = FilterOption.ALL,
        onSortChange = {},
        onFilterChange = {},
        onCreateNewTask = {},
        onTaskClick = {},
        onTaskCompleted = {},
        onTaskDeleted = {},
        onUndoCompleted = {},
        onSettingsClick = {},
        onUndoDeleted = {}
    )
}
