package com.m.ammar.itaskmanager.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * A composable that creates a custom-styled error snackbar.
 *
 * The function utilizes the [SnackbarHost] to display a snackbar with an error theme.
 * The snackbar has the following features:
 * - A rounded corner shape.
 * - A custom background color that reflects the error container.
 * - Text color customized to contrast well on the error background.
 * - An optional action button that performs an action when clicked, if an action label is provided.
 *
 * This composable integrates with a [SnackbarHostState] to manage the visibility
 * and content of the snackbar dynamically within the app's UI.
 *
 * @param hostState The [SnackbarHostState] object that holds the current state of the snackbar,
 *                  including whether it's visible and the message displayed.
 */
@Composable
fun StyledErrorSnackbarHost(hostState: SnackbarHostState) {
    // SnackbarHost to display the snackbar using the provided host state
    SnackbarHost(hostState) { data ->
        val visuals = data.visuals

        // Customizing the appearance of the snackbar
        Snackbar(
            shape = RoundedCornerShape(16.dp), // Adds rounded corners to the snackbar for a modern design
            containerColor = MaterialTheme.colorScheme.errorContainer, // Error-specific background color
            contentColor = MaterialTheme.colorScheme.onErrorContainer, // Color of the text on the error background
            action = {
                visuals.actionLabel?.let { label ->
                    // If an action label is provided, create an action button
                    TextButton(onClick = { data.performAction() }) {
                        Text(text = label, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        ) {
            // Displaying the error message text in the snackbar
            Text(text = visuals.message)
        }
    }
}
