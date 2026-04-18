package com.example.debt.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.example.debt.utils.PreferenceCache
import com.example.debt.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class SettingsViewModel : ViewModel(), KoinComponent {

    private val _autoCountDebts = MutableStateFlow<Boolean>(PreferenceCache.autoCountDebts)
    val autoCountDebts = _autoCountDebts.asStateFlow()

    private val _selectedTheme = MutableStateFlow<ThemeMode>(PreferenceCache.selectedTheme)
    val selectedTheme = _selectedTheme.asStateFlow()


    fun debtAutoCount(v: Boolean) {
        PreferenceCache.autoCountDebts = v
        _autoCountDebts.value = v
    }

    fun setTheme(v: ThemeMode) {
        PreferenceCache.selectedTheme = v
        _selectedTheme.value = v
    }
}