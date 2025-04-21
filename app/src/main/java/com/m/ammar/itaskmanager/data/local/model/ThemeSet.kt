package com.m.ammar.itaskmanager.data.local.model

/**
 * Data class representing a theme set with separate light and dark color schemes.
 *
 * @property name The name of the theme set.
 * @property lightColors A [ColorSet] representing the color scheme for the light mode.
 * @property darkColors A [ColorSet] representing the color scheme for the dark mode.
 */
data class ThemeSet(
    val name: String,
    val lightColors: ColorSet,
    val darkColors: ColorSet
)
