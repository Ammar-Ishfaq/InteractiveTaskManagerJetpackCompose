package com.m.ammar.itaskmanager.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.m.ammar.itaskmanager.data.local.enums.ThemeMode
import com.m.ammar.itaskmanager.navigation.AppNavigation
import com.m.ammar.itaskmanager.ui.settings.SettingsViewModel
import com.m.ammar.itaskmanager.ui.theme.AppTheme

@Composable
fun App(settingsViewModel: SettingsViewModel) {

    val themeMode = settingsViewModel.themeMode.value
    val selectedColorSet = settingsViewModel.selectedColorSet.value

    val systemUiController = rememberSystemUiController()
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val appBarColor = if (themeMode == ThemeMode.SYSTEM) {
        if (isSystemInDarkTheme()) MaterialTheme.colorScheme.inversePrimary
        else MaterialTheme.colorScheme.primary
    } else {
        if (isDark) selectedColorSet.darkColors.primary else selectedColorSet.lightColors.primary
    }
    LaunchedEffect(themeMode, selectedColorSet) {
        systemUiController.setStatusBarColor(color = appBarColor)
    }

    AppTheme(
        themeMode = themeMode,
        selectedThemeSet = selectedColorSet
    ) {
        val navController = rememberNavController()

        Scaffold { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}

