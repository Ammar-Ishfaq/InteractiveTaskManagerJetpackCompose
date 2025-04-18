package com.m.ammar.itaskmanager.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    FilterSortActionButtons(
                        sortOption = sortOption,
                        filterOption = filterOption,
                        onSortChange = onSortChange,
                        onFilterChange = onFilterChange
                    )
                }
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
                    AnimatedVisibility(
                        visible = tasks.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        TaskStatsHeader(tasks = tasks)
                    }

                    HomeScreenContent(
                        tasks = tasks,
                        onTaskClick = onTaskClick
                    )
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
                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )

        // Sort Button
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
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        )
    }

    // Sort Dialog
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

    // Filter Dialog
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
    val overdueCount = tasks.count { it.dueDate < System.currentTimeMillis() && !it.isCompleted }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TaskStatItem(
                count = tasks.size,
                label = "Total",
                painter = painterResource(id = R.drawable.all),
                color = MaterialTheme.colorScheme.primary
            )
            TaskStatItem(
                count = completedCount,
                label = "Done",
                icon = Icons.Default.CheckCircle,
                color = MaterialTheme.colorScheme.tertiary
            )
            TaskStatItem(
                count = overdueCount,
                label = "Overdue",
                icon = Icons.Default.Warning,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun TaskStatItem(count: Int, label: String, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TaskStatItem(count: Int, label: String, painter: Painter, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painter,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    onTaskClick: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TaskItem(task = task, onClick = { onTaskClick(task.id) })
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
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
            .clickable(onClick = onClick),
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


