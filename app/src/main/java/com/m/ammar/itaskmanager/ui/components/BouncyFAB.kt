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

@Composable
fun BouncyFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = {
            scope.launch {
                scale.animateTo(
                    0.8f,
                    animationSpec = tween(100, easing = LinearOutSlowInEasing)
                )
                scale.animateTo(
                    1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            }
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        content = {
            Icon(imageVector = icon, contentDescription = contentDescription)

        }
    )
}

