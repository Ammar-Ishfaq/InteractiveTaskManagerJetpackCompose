package com.m.ammar.itaskmanager.navigation

/**
 * This class represents all the main screens in the app.
 */
sealed class TopLevelDestination(
    val title: String,
    val route: String
) {
    data object CreateTask : TopLevelDestination(
        title = "CreateTask",
        route = "create_task"
    )

    data object Home : TopLevelDestination(
        title = "Home",
        route = "home"
    )

    data object TaskDetail : TopLevelDestination(
        title = "TaskDetail",
        route = "task_detail"
    )
    data object Settings : TopLevelDestination(
        title = "Settings",
        route = "settings"
    )


    /**
     * This is a helper function to pass arguments to navigation destination.
     * For example, instead of using [TopLevelDestination.Detail.route]/first_argument/second_argument
     * you can use like [TopLevelDestination.Detail.withArgs(first_argument, second_argument)]
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
