package com.m.ammar.itaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.m.ammar.itaskmanager.data.enums.ThemeMode
import com.m.ammar.itaskmanager.data.local.model.ThemeSet

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

