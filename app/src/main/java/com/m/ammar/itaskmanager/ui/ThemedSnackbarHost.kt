package com.m.ammar.itaskmanager.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun StyledErrorSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState) { data ->
        val visuals = data.visuals
        Snackbar(
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            action = {
                visuals.actionLabel?.let { label ->
                    TextButton(onClick = { data.performAction() }) {
                        Text(text = label, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        ) {
            Text(text = visuals.message)
        }
    }
}
