package com.m.ammar.itaskmanager.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.m.ammar.itaskmanager.data.local.enums.ThemeMode
import com.m.ammar.itaskmanager.data.local.model.ThemeSet
import com.m.ammar.itaskmanager.ui.theme.ThemeSet1
import com.m.ammar.itaskmanager.ui.theme.ThemeSet2
import com.m.ammar.itaskmanager.ui.theme.ThemeSet3
import com.m.ammar.itaskmanager.ui.theme.ThemeSet4
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    selectedColorSet: ThemeSet,
    onThemeModeChange: (ThemeMode) -> Unit,
    onColorSetChange: (ThemeSet) -> Unit,
    onBack: () -> Unit
) {
    /**
     * Displays the settings screen, including theme mode options (system, light, dark) and custom color theme sets.
     *
     * @param themeMode Current theme mode (light, dark, system).
     * @param selectedColorSet The currently selected color set.
     * @param onThemeModeChange Callback function to update the theme mode.
     * @param onColorSetChange Callback function to update the color theme set.
     * @param onBack Callback function for navigating back.
     */
    val themeSets = listOf(ThemeSet1, ThemeSet2, ThemeSet3, ThemeSet4)
    val isSystemTheme = themeMode == ThemeMode.SYSTEM

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            content = {
                item {
                    Text("Theme Preference", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))

                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text("System Theme", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            ThemeModeItem(
                                themeMode = themeMode,
                                mode = ThemeMode.SYSTEM,
                                onClick = onThemeModeChange,
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Text("Custom Appearance", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))

                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text("Select Mode", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            ThemeMode.entries
                                .filter { it != ThemeMode.SYSTEM }
                                .forEach { mode ->
                                    ThemeModeItem(
                                        themeMode = themeMode,
                                        mode = mode,
                                        onClick = {
                                            onThemeModeChange(mode)
                                            if (selectedColorSet !in listOf(
                                                    ThemeSet1, ThemeSet2, ThemeSet3, ThemeSet4
                                                )
                                            ) {
                                                onColorSetChange(ThemeSet1)
                                            }
                                        },
                                    )
                                }

                            Spacer(Modifier.height(16.dp))
                            Divider()
                            Spacer(Modifier.height(16.dp))

                            Text(
                                "Select a Color Theme",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))

                            themeSets.forEach { colorSet ->
                                ColorSetItem(
                                    colorSet = colorSet,
                                    selectedColorSet = selectedColorSet,
                                    themeMode = themeMode,
                                    onClick = onColorSetChange,
                                    enabled = !isSystemTheme
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun ThemeModeItem(
    themeMode: ThemeMode,
    mode: ThemeMode,
    onClick: (ThemeMode) -> Unit,
) {
    /**
     * Displays a single theme mode option (RadioButton) and handles user selection.
     *
     * @param themeMode Current theme mode (light, dark, system).
     * @param mode The theme mode option to display.
     * @param onClick Callback function to update the theme mode.
     */
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(mode) }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = themeMode == mode,
            onClick = { onClick(mode) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
    }
}

@Composable
private fun ColorSetItem(
    colorSet: ThemeSet,
    selectedColorSet: ThemeSet,
    themeMode: ThemeMode,
    onClick: (ThemeSet) -> Unit,
    enabled: Boolean = true
) {
    /**
     * Displays a single color set option as a circular color preview and handles user selection.
     *
     * @param colorSet The color set to display.
     * @param selectedColorSet The currently selected color set.
     * @param themeMode Current theme mode (light or dark).
     * @param onClick Callback function to change the selected color set.
     * @param enabled Flag to indicate whether the item is selectable.
     */
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled) { if (enabled) onClick(colorSet) }
            .padding(vertical = 8.dp)
            .alpha(if (enabled) 1f else 0.5f)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (themeMode == ThemeMode.LIGHT)
                        colorSet.lightColors.primary
                    else
                        colorSet.darkColors.primary
                )
                .border(
                    width = if (colorSet == selectedColorSet) 3.dp else 1.dp,
                    color = if (colorSet == selectedColorSet) Color.Black else Color.Gray,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(colorSet.name)
    }
}
