package com.m.ammar.itaskmanager.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.m.ammar.itaskmanager.data.local.model.ColorSet

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


fun colorSchemeFromColorSet(
    colorSet: ColorSet,
    isDark: Boolean
): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = colorSet.primary,
            onPrimary = Color.White,
            primaryContainer = colorSet.primary,
            onPrimaryContainer = Color.White,
            secondary = colorSet.secondary,
            onSecondary = Color.White,
            tertiary = colorSet.tertiary,
            background = colorSet.background,
            onBackground = Color.White,
            surface = colorSet.surface,
            onSurface = Color.White,
            surfaceVariant = colorSet.surface,
            onSurfaceVariant = Color.White
        )
    } else {
        lightColorScheme(
            primary = colorSet.primary,
            onPrimary = Color.Black,
            primaryContainer = colorSet.primary,
            onPrimaryContainer = Color.White,
            secondary = colorSet.secondary,
            onSecondary = Color.Black,
            tertiary = colorSet.tertiary,
            background = colorSet.background,
            onBackground = Color.Black,
            surface = colorSet.surface,
            onSurface = Color.Black,
            surfaceVariant = colorSet.surface,
            onSurfaceVariant = Color.Black
        )
    }
}
