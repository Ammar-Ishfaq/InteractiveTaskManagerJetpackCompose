package com.m.ammar.itaskmanager.navigation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    val viewModel: HomeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        composable(route = TopLevelDestination.Home.route) {

            val uiState by remember { viewModel.response }.collectAsStateWithLifecycle()
            val tasks by viewModel.tasksFlow.collectAsState()
            val currentSort by viewModel.sortOption.collectAsState()
            val currentFilter by viewModel.filterOption.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadData()
            }

            HomeScreen(
                uiState = uiState,
                tasks = tasks,
                sortOption = currentSort,
                filterOption = currentFilter,
                onSortChange = { viewModel.setSortOption(it) },
                onFilterChange = { viewModel.setFilterOption(it) },
                onCreateNewTask = { navController.navigate(TopLevelDestination.CreateTask.route) },
                onTaskClick = { task ->
                    viewModel.selectTask(task)
                    navController.navigate(TopLevelDestination.TaskDetail.route)
                },
                onTaskCompleted = {
                    viewModel.updateTask(it.copy(isCompleted = true))
                },
                onTaskDeleted = {
                    viewModel.deleteTask(it)
                },
                onUndo = {
                    viewModel.updateTask(it)
                },
                onSettingsClick = {
                    navController.navigate(TopLevelDestination.Settings.route)

                }
            )
        }

        composable(route = TopLevelDestination.CreateTask.route) {

            TaskCreationScreen(
                onSaveClick = { title, description, priority, dueDate ->
                    val task = Task(
                        title = title,
                        description = description,
                        priority = priority,
                        dueDate = dueDate,
                        isCompleted = false
                    )
                    viewModel.addTask(task)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )

        }
        composable(route = TopLevelDestination.TaskDetail.route) {
            val task by viewModel.selectedTask.collectAsState()

            task?.let {
                TaskDetailsScreen(
                    task = it,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        viewModel.deleteTask(it)
                        navController.popBackStack()
                    },
                    onComplete = {
                        viewModel.updateTask(it.copy(isCompleted = true))
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
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}
