package com.m.ammar.itaskmanager.data.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.m.ammar.itaskmanager.data.enums.ThemeMode
import com.m.ammar.itaskmanager.ui.theme.ThemeSet1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {
    private val dataStore = context.dataStore

    // Keys for theme mode and selected theme set
    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    private val SELECTED_THEME_SET_KEY = stringPreferencesKey("selected_theme_set")

    // Get theme mode (Light, Dark, or System)
    val themeMode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name // Default to SYSTEM
        }

    val selectedThemeSet: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SELECTED_THEME_SET_KEY] ?: ThemeSet1.name
        }

    // Save theme mode
    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }

    // Save selected theme set name
    suspend fun saveSelectedThemeSet(themeSetName: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_THEME_SET_KEY] = themeSetName
        }
    }
}
