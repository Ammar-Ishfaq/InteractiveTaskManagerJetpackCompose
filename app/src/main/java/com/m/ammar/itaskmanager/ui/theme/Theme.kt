package com.m.ammar.itaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.m.ammar.itaskmanager.data.local.enums.ThemeMode
import com.m.ammar.itaskmanager.data.local.model.ThemeSet
/**
 * Composable function to apply a theme based on the selected theme mode and theme set.
 *
 * This function applies the appropriate color scheme based on the theme mode (light, dark, or system default)
 * and the selected theme set. It updates the MaterialTheme with the relevant color scheme and typography.
 *
 * @param themeMode The mode for the theme (LIGHT, DARK, or SYSTEM).
 * @param selectedThemeSet The theme set containing color definitions for light and dark modes.
 * @param content A composable lambda that represents the content of the screen, which will be styled according to the selected theme.
 */
@Composable
fun AppTheme(
    themeMode: ThemeMode,
    selectedThemeSet: ThemeSet,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when (themeMode) {
        ThemeMode.SYSTEM -> if (isDark) darkColorScheme() else lightColorScheme()
        else -> {
            val colorSet = if (isDark) selectedThemeSet.darkColors else selectedThemeSet.lightColors
            if (isDark) {
                darkColorScheme(
                    primary = colorSet.primary,
                    secondary = colorSet.secondary,
                    tertiary = colorSet.tertiary,
                    background = colorSet.background,
                    surface = colorSet.surface,
                    surfaceVariant = colorSet.surfaceVariant,
                    onSurfaceVariant = colorSet.onSurfaceVariant,
                    primaryContainer = colorSet.primaryContainer,
                    onPrimaryContainer = colorSet.onPrimaryContainer,
                )
            } else {
                lightColorScheme(
                    primary = colorSet.primary,
                    secondary = colorSet.secondary,
                    tertiary = colorSet.tertiary,
                    background = colorSet.background,
                    surface = colorSet.surface,
                    surfaceVariant = colorSet.surfaceVariant,
                    onSurfaceVariant = colorSet.onSurfaceVariant,
                    primaryContainer = colorSet.primaryContainer,
                    onPrimaryContainer = colorSet.onPrimaryContainer,
                )
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
