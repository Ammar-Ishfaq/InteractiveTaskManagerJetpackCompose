package com.m.ammar.itaskmanager.data.local.enums
/**
 * Enum class representing different sorting options for tasks.
 */
enum class SortOption {
    PRIORITY,   // Sort by priority of tasks
    DUE_DATE,   // Sort by due date of tasks
    ALPHABETICAL // Sort tasks alphabetically by title
}

/**
 * Enum class representing different filter options for tasks.
 */
enum class FilterOption {
    ALL,        // Show all tasks
    COMPLETED,  // Show only completed tasks
    PENDING     // Show only pending tasks
}

/**
 * Enum class representing the theme mode.
 *
 * The theme can be set to follow the system, light, or dark mode.
 */
enum class ThemeMode {
    SYSTEM,  // Follow the system's theme mode
    LIGHT,   // Use light theme
    DARK     // Use dark theme
}
