package com.example.debt.ui.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.debt.app.ui.screens.MainUserScreen
import com.example.debt.ui.theme.DebtDiaryTheme
import com.example.debt.utils.AppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainUserScreen()
            }
        }
    }
}
