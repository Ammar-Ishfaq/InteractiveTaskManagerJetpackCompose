package com.m.ammar.itaskmanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.ammar.itaskmanager.R

/**
 * Composable function that displays an empty task view with an animation and message.
 * It shows an animation and prompts the user to create a new task.
 */
@Composable
fun EmptyTaskView() {
    // Lottie animation composition for empty task state
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_task))
    // Progress for the animation
    val progress by animateLottieCompositionAsState(composition)

    // Box layout to center the content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Column to stack the animation and texts vertically
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lottie animation for the empty task state
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .height(250.dp)
                    .padding(16.dp)
            )

            // Text to show the "No tasks yet" message
            Text(
                text = "No tasks yet!",
                style = MaterialTheme.typography.titleMedium
            )

            // Text to encourage task creation
            Text(
                text = "Create a new task to get started.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
