package com.m.ammar.itaskmanager.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.enums.ThemeMode
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
) :
    ViewModel() {

    private val _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: State<ThemeMode> get() = _themeMode

    private val _selectedColorSet = mutableStateOf(ThemeSet1)
    val selectedColorSet: State<ThemeSet> get() = _selectedColorSet

        init {
//    fun loadTheme() {
        viewModelScope.launch {
            settingsRepository.themeMode.collect { mode ->
                _themeMode.value = ThemeMode.valueOf(mode)
            }
        }

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

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            _themeMode.value = mode
            settingsRepository.saveThemeMode(mode.name)
        }
    }

    fun updateColorSet(colorSet: ThemeSet) {
        viewModelScope.launch {
            _selectedColorSet.value = colorSet
            settingsRepository.saveSelectedThemeSet(colorSet.name)
        }
    }
}
