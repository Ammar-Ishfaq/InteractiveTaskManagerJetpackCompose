package com.m.ammar.itaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.m.ammar.itaskmanager.presentation.App
import com.m.ammar.itaskmanager.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point for the app.
 * It initializes the SettingsViewModel and provides it to the Composables via the App composable.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // ViewModel for managing app settings
    private val settingsViewModel: SettingsViewModel by viewModels()

    /**
     * Called when the activity is created. Sets the content view of the activity.
     * Initializes the App composable and provides the SettingsViewModel.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Provides SettingsViewModel to the App composable
            App(settingsViewModel)
        }
    }
}
