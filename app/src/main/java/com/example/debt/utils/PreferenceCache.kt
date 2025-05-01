package com.example.debt.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.example.debt.App
import com.example.debt.app.utils.LogUtils.errorLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object PreferenceCache {
    private val prefs: SharedPreferences = App.appInstance.getSharedPreferences(
        "THEME_PREFERENCES",
        Context.MODE_PRIVATE
    )

    private const val SELECTED_THEME: String = "selected_theme"

    @set:Synchronized
    var themeChanged = mutableStateOf(0)
        private set

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
}