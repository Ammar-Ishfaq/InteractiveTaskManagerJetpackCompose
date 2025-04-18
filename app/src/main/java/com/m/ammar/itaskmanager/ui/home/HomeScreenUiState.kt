package com.m.ammar.itaskmanager.ui.home

import com.m.ammar.itaskmanager.data.local.model.Task

/**
 * Sealed class to represent UI states in [HomeScreen]
 */
sealed interface HomeScreenUiState {
    data object Initial : HomeScreenUiState
    data object Empty : HomeScreenUiState
    data class Success(val tasks: List<Task>) : HomeScreenUiState
    data class Error(val msg: String) : HomeScreenUiState
}
