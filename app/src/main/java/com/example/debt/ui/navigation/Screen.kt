package com.example.debt.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AppSettings : Screen("settings")
}