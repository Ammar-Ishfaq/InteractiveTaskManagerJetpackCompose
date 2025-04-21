package com.m.ammar.itaskmanager.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.launch

/**
 * A composable function that creates a bouncing FloatingActionButton (FAB).
 * The button scales down and back up when clicked, with a haptic feedback effect.
 *
 * @param onClick Lambda function to be executed when the FAB is clicked.
 * @param icon The icon to display inside the FAB.
 * @param contentDescription Description of the icon, for accessibility purposes.
 */
@Composable
fun BouncyFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    // Animatable state to handle the scaling animation of the FAB
    val scale = remember { Animatable(1f) }
    // Coroutine scope for launching animations
    val scope = rememberCoroutineScope()
    // Haptic feedback controller
    val haptic = LocalHapticFeedback.current

    // FloatingActionButton with scaling and haptic feedback on click
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = {
            // Launching the scaling animation when the FAB is clicked
            scope.launch {
                scale.animateTo(
                    0.8f,
                    animationSpec = tween(100, easing = LinearOutSlowInEasing) // First scale down
                )
                scale.animateTo(
                    1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy) // Then scale up
                )
            }
            // Perform haptic feedback to provide tactile feedback
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            // Execute the provided onClick lambda
            onClick()
        },
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        content = {
            // Display the icon inside the FAB
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    )
}
