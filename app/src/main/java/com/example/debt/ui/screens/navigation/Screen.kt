package com.example.debt.ui.screens.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AppSettings : Screen("settings")
}