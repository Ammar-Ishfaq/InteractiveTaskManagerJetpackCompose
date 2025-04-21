package com.m.ammar.itaskmanager.navigation

/**
 * This class represents all the main screens in the app.
 */
sealed class TopLevelDestination(
    val title: String,
    val route: String
) {

    /** Represents the "Create Task" screen. */
    data object CreateTask : TopLevelDestination(
        title = "CreateTask",
        route = "create_task"
    )

    /** Represents the "Home" screen where tasks are listed. */
    data object Home : TopLevelDestination(
        title = "Home",
        route = "home"
    )

    /** Represents the "Task Detail" screen for viewing and managing a specific task. */
    data object TaskDetail : TopLevelDestination(
        title = "TaskDetail",
        route = "task_detail"
    )

    /** Represents the "Settings" screen for configuring app preferences. */
    data object Settings : TopLevelDestination(
        title = "Settings",
        route = "settings"
    )


    /**
     * Helper function to construct a navigation route with arguments.
     *
     * Instead of using a hardcoded route with arguments like
     * `TopLevelDestination.TaskDetail.route/first_argument/second_argument`,
     * you can use this function to easily build a dynamic route:
     * `TopLevelDestination.TaskDetail.withArgs(first_argument, second_argument)`.
     *
     * @param args Arguments to be appended to the route.
     * @return The constructed route with arguments.
     */
    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
