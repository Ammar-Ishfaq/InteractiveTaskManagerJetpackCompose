package com.m.ammar.itaskmanager.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.m.ammar.itaskmanager.data.local.model.Priority
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.utility.toReadableDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Displays a header card with a circular progress indicator showing
 * the percentage of completed tasks out of the total tasks.
 *
 * @param tasks List of [Task] items to calculate completion percentage.
 */
@Composable
fun TaskStatsHeader(tasks: List<Task>) {
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

/**
 * Displays a swipeable task item with actions:
 * - Swipe start-to-end to complete the task.
 * - Swipe end-to-start to delete the task.
 * Includes snackbars for undoing actions.
 *
 * @param scope Coroutine scope for snackbar and async operations.
 * @param task Task to display.
 * @param snackbarHostState Snackbar host to show feedback messages.
 * @param onDelete Callback when task is deleted.
 * @param onComplete Callback when task is marked as complete.
 * @param onUndoCompleted Callback to undo completed status.
 * @param onUndoDeleted Callback to undo deletion.
 * @param onClick Callback when task item is clicked.
 */
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
                    SwipeToDismissBoxValue.StartToEnd -> if (task.isCompleted.not()) Color(
                        0xFF388E3C
                    ) else Color.Transparent

                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F)
                    else -> Color.Transparent
                }
            )

            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> if (task.isCompleted.not()) Icons.Default.Check else null
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
        enableDismissFromStartToEnd = task.isCompleted.not(),
        enableDismissFromEndToStart = true,
        gesturesEnabled = true
    ) {
        TaskItem(task = task, onClick = onClick)
    }
}

/**
 * Displays an individual task in a card layout.
 * Shows title, description, due date, and priority badge.
 * Visually indicates completed or overdue status.
 *
 * @param task Task to display.
 * @param onClick Callback when the task item is tapped.
 */

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