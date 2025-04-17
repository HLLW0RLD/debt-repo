package com.example.debt.ui.debt.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.debt.app.ui.screens.MainUserScreen
import com.example.debt.ui.debt.DebtDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DebtDiaryTheme {
                MainUserScreen()
            }
        }
    }
}
