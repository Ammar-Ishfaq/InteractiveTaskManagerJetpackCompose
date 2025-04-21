package com.m.ammar.itaskmanager.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.m.ammar.itaskmanager.data.SnackbarEvent
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.task_detail.TaskDetailsScreen
import com.m.ammar.itaskmanager.ui.task_creation.TaskCreationScreen
import com.m.ammar.itaskmanager.ui.home.HomeScreen
import com.m.ammar.itaskmanager.ui.home.HomeViewModel
import com.m.ammar.itaskmanager.ui.settings.SettingsScreen
import com.m.ammar.itaskmanager.ui.settings.SettingsViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        composable(route = TopLevelDestination.Home.route) {
            val tasks by homeViewModel.tasksFlow.collectAsStateWithLifecycle()
            val currentSort by homeViewModel.sortOption.collectAsStateWithLifecycle()
            val currentFilter by homeViewModel.filterOption.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                homeViewModel.loadData()
            }


            HomeScreen(
                tasks = tasks,
                sortOption = currentSort,
                filterOption = currentFilter,
                onSortChange = { homeViewModel.setSortOption(it) },
                onFilterChange = { homeViewModel.setFilterOption(it) },
                onCreateNewTask = { navController.navigate(TopLevelDestination.CreateTask.route) },
                onTaskClick = { task ->
                    homeViewModel.selectTask(task)
                    navController.navigate(TopLevelDestination.TaskDetail.route)
                },
                onTaskCompleted = { homeViewModel.completeTask(it) },
                onTaskDeleted = { homeViewModel.deleteTask(it) },
                onUndoCompleted = { homeViewModel.updateTask(it.copy(isCompleted = false)) },
                onUndoDeleted = { homeViewModel.undoDelete() },
                onSettingsClick = { navController.navigate(TopLevelDestination.Settings.route) }
            )

        }

        composable(route = TopLevelDestination.CreateTask.route) {
            TaskCreationScreen(
                onSaveClick = { title, description, priority, dueDate ->
                    homeViewModel.addTask(
                        Task(
                            title = title,
                            description = description,
                            priority = priority,
                            dueDate = dueDate,
                            isCompleted = false
                        )
                    )
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = TopLevelDestination.TaskDetail.route) {
            val task by homeViewModel.selectedTask.collectAsStateWithLifecycle()

            task?.let { nonNullTask ->
                TaskDetailsScreen(
                    task = nonNullTask,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        homeViewModel.deleteTask(nonNullTask)
                        navController.popBackStack()
                    },
                    onComplete = {
                        homeViewModel.completeTask(nonNullTask)
                    }
                )
            }
        }

        composable(route = TopLevelDestination.Settings.route) {
            SettingsScreen(
                themeMode = settingsViewModel.themeMode.value,
                selectedColorSet = settingsViewModel.selectedColorSet.value,
                onThemeModeChange = { settingsViewModel.updateThemeMode(it) },
                onColorSetChange = { settingsViewModel.updateColorSet(it) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}