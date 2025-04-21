package com.m.ammar.itaskmanager.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.ammar.itaskmanager.R
import com.m.ammar.itaskmanager.data.SnackbarEvent
import com.m.ammar.itaskmanager.data.enums.FilterOption
import com.m.ammar.itaskmanager.data.enums.SortOption
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.components.BouncyFAB
import com.m.ammar.itaskmanager.ui.components.ErrorItem
import com.m.ammar.itaskmanager.utility.toReadableDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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
private fun FilterSortActionButtons(
    sortOption: SortOption,
    filterOption: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit
) {
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        // Filter Button
        FilterChip(
            selected = filterOption != FilterOption.ALL,
            onClick = { showFilterDialog = true },
            label = {
                Text(
                    text = "Filter",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = "Filter",
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        FilterChip(
            selected = sortOption != SortOption.DUE_DATE,
            onClick = { showSortDialog = true },
            label = {
                Text(
                    text = "Sort",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Sort",
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }

    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Sort Tasks") },
            text = {
                Column {
                    SortOption.entries.forEach { option ->
                        RadioButtonItem(
                            text = option.name,
                            selected = option == sortOption,
                            onSelect = {
                                onSortChange(option)
                                showSortDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("DONE")
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Tasks") },
            text = {
                Column {
                    FilterOption.entries.forEach { option ->
                        RadioButtonItem(
                            text = option.name,
                            selected = option == filterOption,
                            onSelect = {
                                onFilterChange(option)
                                showFilterDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("DONE")
                }
            }
        )
    }
}

@Composable
private fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TaskStatsHeader(tasks: List<Task>) {
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size

    val completionPercent = if (totalCount == 0) 0f else completedCount / totalCount.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = completionPercent,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = 1f,
                strokeWidth = 10.dp,
                modifier = Modifier.size(120.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            CircularProgressIndicator(
                progress = animatedProgress,
                strokeWidth = 10.dp,
                modifier = Modifier.size(120.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(completionPercent * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

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

@Composable
fun SwipeableTaskItem(
    scope: CoroutineScope,
    task: Task,
    snackbarHostState: SnackbarHostState,
    onDelete: (Task) -> Unit,
    onComplete: (Task) -> Unit,
    onUndoCompleted: (Task) -> Unit,
    onUndoDeleted: (Task) -> Unit,
    onClick: (Task) -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onComplete(task)

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Task completed",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            onUndoCompleted(task)
                        }
                    }
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete(task)

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Task deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            onUndoDeleted(task)
                        }
                    }
                    false
                }

                else -> false
            }
        }
    )


    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.dismissDirection) {
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF388E3C)
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F)
                    else -> Color.Transparent
                }
            )

            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Check
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> null
            }

            val alignment = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.CenterEnd
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(alignment)
                    )
                }
            }
        },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        gesturesEnabled = true
    ) {
        TaskItem(task = task, onClick = onClick)
    }
}

@Composable
fun TaskItem(task: Task, onClick: (Task) -> Unit) {
    val isOverdue = !task.isCompleted && task.dueDate < System.currentTimeMillis()
    val priorityColor = when (task.priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.errorContainer
        Priority.MEDIUM -> MaterialTheme.colorScheme.tertiaryContainer
        Priority.LOW -> MaterialTheme.colorScheme.secondaryContainer
    }
    val priorityContentColor = when (task.priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.onErrorContainer
        Priority.MEDIUM -> MaterialTheme.colorScheme.onTertiaryContainer
        Priority.LOW -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(task) }),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (isOverdue) {
                    Text(
                        text = "OVERDUE",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            task.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(priorityColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = task.priority.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityContentColor
                    )
                }

                Text(
                    text = "Due ${task.dueDate.toReadableDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isOverdue) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
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


