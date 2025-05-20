package com.example.debt.ui.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.debt.app.ui.screens.MainUserScreen
import com.example.debt.ui.navigation.Screen
import com.example.debt.ui.screens.settings.AppSettingsScreen
import com.example.debt.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Main.route
                ) {
                    composable(Screen.Main.route) {
                        MainUserScreen(
                            onSettingsClick = { navController.navigate(Screen.AppSettings.route) }
                        )
                    }
                    composable(Screen.AppSettings.route) {
                        AppSettingsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
