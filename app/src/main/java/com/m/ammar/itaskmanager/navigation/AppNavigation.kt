package com.m.ammar.itaskmanager.navigation

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
import com.m.ammar.itaskmanager.data.local.model.Task
import com.m.ammar.itaskmanager.ui.create_task.TaskCreationScreen
import com.m.ammar.itaskmanager.ui.home.HomeScreen
import com.m.ammar.itaskmanager.ui.home.HomeViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        composable(route = TopLevelDestination.Home.route) {

            val viewModel: HomeViewModel = hiltViewModel()
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
                onTaskClick = { }
            )
        }

        composable(route = TopLevelDestination.CreateTask.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()

            TaskCreationScreen { title, description, priority, dueDate ->
                val task = Task(
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    isCompleted = false
                )
                homeViewModel.addTask(task)
                navController.popBackStack()
            }

        }
    }
}
