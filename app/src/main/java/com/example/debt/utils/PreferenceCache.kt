package com.example.debt.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.example.debt.App
import com.example.debt.app.utils.LogUtils.errorLog

object PreferenceCache {
    private val prefs: SharedPreferences = App.appInstance.getSharedPreferences(
        "THEME_PREFERENCES",
        Context.MODE_PRIVATE
    )

    private const val SELECTED_THEME: String = "selected_theme"
    private const val AUTO_SETTLE_DEBTS: String = "auto_settle_debts"
    private const val AUTO_DELETE_EMPTY_DEBTS: String = "auto_delete_empty_debts"

    @set:Synchronized
    var themeChanged = mutableStateOf(0)
        private set

    val isColoredTheme get() = selectedTheme == ThemeMode.COLOR
    val isDarkTheme get() = selectedTheme == ThemeMode.DARK

    @set:Synchronized
    var selectedTheme: ThemeMode
        get() = try {
            ThemeMode.valueOf(prefs.getString(SELECTED_THEME, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            errorLog("Failed to get theme\n$e")
            ThemeMode.SYSTEM
        }
        set(value) {
            prefs.edit().putString(SELECTED_THEME, value.name).apply()
            themeChanged.value++
        }

    @set:Synchronized
    var autoCountDebts: Boolean
        get() { return prefs.getBoolean(AUTO_SETTLE_DEBTS, true) }
        set(value) { prefs.edit().putBoolean(AUTO_SETTLE_DEBTS, value )?.apply() }

    @set:Synchronized
    var autoDeleteEmptyDebts: Boolean
        get() { return prefs.getBoolean(AUTO_DELETE_EMPTY_DEBTS, true) }
        set(value) { prefs.edit().putBoolean(AUTO_DELETE_EMPTY_DEBTS, value )?.apply() }

}