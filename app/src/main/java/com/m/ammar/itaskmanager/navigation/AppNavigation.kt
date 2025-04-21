package com.m.ammar.itaskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.home.HomeScreen
import com.m.ammar.itaskmanager.ui.home.HomeViewModel
import com.m.ammar.itaskmanager.ui.settings.SettingsScreen
import com.m.ammar.itaskmanager.ui.settings.SettingsViewModel
import com.m.ammar.itaskmanager.ui.task_creation.TaskCreationScreen
import com.m.ammar.itaskmanager.ui.task_detail.TaskDetailsScreen

/**
 * Composable function that handles the app's navigation.
 * Sets up navigation and routing between different screens of the app.
 *
 * @param modifier Modifier to customize the layout.
 * @param navController The navigation controller used for managing navigation actions.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    // Initialize ViewModels using Hilt
    val homeViewModel: HomeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    // Define the navigation host with routes to all major destinations
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        // Composable for the Home screen
        composable(route = TopLevelDestination.Home.route) {
            // Collect current data from HomeViewModel using lifecycle-aware state
            val tasks by homeViewModel.tasksFlow.collectAsStateWithLifecycle()
            val currentSort by homeViewModel.sortOption.collectAsStateWithLifecycle()
            val currentFilter by homeViewModel.filterOption.collectAsStateWithLifecycle()

            // Load data when the Home screen is first launched
            LaunchedEffect(Unit) {
                homeViewModel.loadData()
            }

            // Display the HomeScreen composable
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

        // Composable for the Task Creation screen
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

        // Composable for the Task Detail screen
        composable(route = TopLevelDestination.TaskDetail.route) {
            // Collect selected task from HomeViewModel
            val task by homeViewModel.selectedTask.collectAsStateWithLifecycle()

            // Display the TaskDetailsScreen if a task is selected
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

        // Composable for the Settings screen
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
