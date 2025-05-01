package com.example.debt.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.debt.App
import com.example.debt.app.utils.LogUtils.errorLog

object PreferenceCache {
    private var prefs: SharedPreferences = App.appInstance.getSharedPreferences(
        "THEME_PREFERENCES",
        Context.MODE_PRIVATE
    )

    private const val SELECTED_THEME: String = "selected_theme"
    private const val IS_COLOR_THEME = "is_color_theme"

    enum class ThemeMode {
        SYSTEM, LIGHT, DARK, COLOR
    }

    @set:Synchronized
    var selectedTheme: ThemeMode
        get() = try {
            ThemeMode.valueOf(prefs.getString(SELECTED_THEME, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            errorLog("Failed to get selected theme becouse:\n$e")
            ThemeMode.SYSTEM
        }
        set(value) {
            if (value == ThemeMode.COLOR) isColorTheme = true else isColorTheme = false
            prefs.edit().putString(SELECTED_THEME, value.name).apply()
        }

    @set:Synchronized
    var isColorTheme: Boolean
        get() = prefs.getBoolean(IS_COLOR_THEME, false)
        set(value) {
            if (value == true) selectedTheme = ThemeMode.COLOR
            prefs.edit().putBoolean(IS_COLOR_THEME, value).apply()
        }
}