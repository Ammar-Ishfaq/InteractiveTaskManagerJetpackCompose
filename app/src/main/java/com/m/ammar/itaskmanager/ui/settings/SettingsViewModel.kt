package com.m.ammar.itaskmanager.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.local.enums.ThemeMode
import com.m.ammar.itaskmanager.data.local.data_store.SettingsRepository
import com.m.ammar.itaskmanager.data.local.model.ThemeSet
import com.m.ammar.itaskmanager.ui.theme.ThemeSet1
import com.m.ammar.itaskmanager.ui.theme.ThemeSet2
import com.m.ammar.itaskmanager.ui.theme.ThemeSet3
import com.m.ammar.itaskmanager.ui.theme.ThemeSet4
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsRepository: SettingsRepository
) : ViewModel() {

    /**
     * Mutable state holding the current theme mode (System, Light, or Dark).
     * Exposed as immutable state via [themeMode].
     */
    private val _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: State<ThemeMode> get() = _themeMode

    /**
     * Mutable state holding the currently selected color set.
     * Exposed as immutable state via [selectedColorSet].
     */
    private val _selectedColorSet = mutableStateOf(ThemeSet1)
    val selectedColorSet: State<ThemeSet> get() = _selectedColorSet

    init {
        // Collects the theme mode value from the repository and updates the UI state
        viewModelScope.launch {
            settingsRepository.themeMode.collect { mode ->
                _themeMode.value = ThemeMode.valueOf(mode)
            }
        }

        // Collects the selected theme set from the repository and updates the UI state
        viewModelScope.launch {
            settingsRepository.selectedThemeSet.collect { themeSetName ->
                _selectedColorSet.value = when (themeSetName) {
                    ThemeSet1.name -> ThemeSet1
                    ThemeSet2.name -> ThemeSet2
                    ThemeSet3.name -> ThemeSet3
                    ThemeSet4.name -> ThemeSet4
                    else -> ThemeSet1
                }
            }
        }
    }

    /**
     * Updates the theme mode in both the view model state and the repository.
     *
     * @param mode The new theme mode to set (e.g., Light, Dark, System).
     */
    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            _themeMode.value = mode
            settingsRepository.saveThemeMode(mode.name)
        }
    }

    /**
     * Updates the selected color set in both the view model state and the repository.
     *
     * @param colorSet The new color set to apply (e.g., ThemeSet1, ThemeSet2).
     */
    fun updateColorSet(colorSet: ThemeSet) {
        viewModelScope.launch {
            _selectedColorSet.value = colorSet
            settingsRepository.saveSelectedThemeSet(colorSet.name)
        }
    }
}
