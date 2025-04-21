package com.m.ammar.itaskmanager.data.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m.ammar.itaskmanager.data.local.enums.ThemeMode
import com.m.ammar.itaskmanager.ui.theme.ThemeSet1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * SettingsRepository handles storing and retrieving user settings
 * related to theme mode (Light, Dark, or System) and selected theme set.
 *
 * @param context The application context used to access DataStore.
 */
class SettingsRepository(context: Context) {
    private val dataStore = context.dataStore

    // Keys for theme mode and selected theme set
    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    private val SELECTED_THEME_SET_KEY = stringPreferencesKey("selected_theme_set")

    /**
     * Flow for retrieving the current theme mode (Light, Dark, or System).
     * Defaults to System theme if not set.
     */
    val themeMode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name // Default to SYSTEM
        }

    /**
     * Flow for retrieving the currently selected theme set.
     * Defaults to ThemeSet1 if not set.
     */
    val selectedThemeSet: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SELECTED_THEME_SET_KEY] ?: ThemeSet1.name
        }

    /**
     * Save the theme mode to DataStore.
     *
     * @param mode The theme mode to save (Light, Dark, or System).
     */
    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }

    /**
     * Save the selected theme set to DataStore.
     *
     * @param themeSetName The name of the theme set to save.
     */
    suspend fun saveSelectedThemeSet(themeSetName: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_THEME_SET_KEY] = themeSetName
        }
    }
}
