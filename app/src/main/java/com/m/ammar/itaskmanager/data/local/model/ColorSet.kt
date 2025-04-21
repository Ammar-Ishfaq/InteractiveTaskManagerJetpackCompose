package com.m.ammar.itaskmanager.data.local.model

import androidx.compose.ui.graphics.Color


/**
 * Data class representing a set of colors for a theme.
 *
 * @param primary The primary color used in the theme.
 * @param secondary The secondary color used in the theme.
 * @param tertiary The tertiary color used in the theme.
 * @param background The background color used in the theme.
 * @param surface The surface color used in the theme.
 * @param surfaceVariant The variant of the surface color used in the theme.
 * @param onSurfaceVariant The color used for content (like text or icons) on the surface variant.
 * @param primaryContainer The color for the primary container.
 * @param onPrimaryContainer The color used for content (like text or icons) on the primary container.
 */
data class ColorSet(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color
)
