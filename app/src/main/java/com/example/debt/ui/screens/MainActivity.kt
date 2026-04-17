package com.example.debt.ui.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.debt.app.ui.screens.DebtorsFeed
import com.example.debt.app.ui.screens.DebtorsFeedScreen
import com.example.debt.ui.screens.settings.Settings
import com.example.debt.ui.screens.settings.SettingsScreen
import com.example.debt.ui.theme.AppTheme
import com.example.debt.utils.LocalNavController
import com.example.debt.utils.animatedComposable

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalNavController provides navController,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = DebtorsFeed
                    ) {
                        animatedComposable<DebtorsFeed>(
                            navController = navController
                        ) {
                            DebtorsFeedScreen()
                        }
                        animatedComposable<Settings>(
                            navController = navController
                        ) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}
