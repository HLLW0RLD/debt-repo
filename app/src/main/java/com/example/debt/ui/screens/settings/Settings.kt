package com.example.debt.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debt.R
import com.example.debt.ui.items.DebtRadioButton
import com.example.debt.ui.theme.AppColors
import com.example.debt.ui.theme.ThemeMode
import com.example.debt.utils.LocalNavController
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Settings

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {

    val navController = LocalNavController.current

    val debtAutoCount by settingsViewModel.autoCountDebts.collectAsState()
    val debtAutoDelete by settingsViewModel.debtAutoDelete.collectAsState()
    val selectedTheme by settingsViewModel.selectedTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
    ) {
        Spacer(Modifier.size(50.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(AppColors.background)
                .fillMaxWidth(),
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = AppColors.textPrimary,
                    contentDescription = "",
                )
            }
            Text(
                text = stringResource(R.string.settings_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.textPrimary,
            )
        }

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.theme_selection),
                color = AppColors.textPrimary,
                modifier = Modifier.padding(8.dp)
            )

            ThemeOption(
                text = stringResource(R.string.theme_system),
                selected = selectedTheme == ThemeMode.SYSTEM,
                onClick = {
                    settingsViewModel.setTheme(ThemeMode.SYSTEM)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_light),
                selected = selectedTheme == ThemeMode.LIGHT,
                onClick = {
                    settingsViewModel.setTheme(ThemeMode.LIGHT)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_dark),
                selected = selectedTheme == ThemeMode.DARK,
                onClick = {
                    settingsViewModel.setTheme(ThemeMode.DARK)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_colored),
                selected = selectedTheme == ThemeMode.COLOR,
                onClick = {
                    settingsViewModel.setTheme(ThemeMode.COLOR)
                }
            )
        }

        Spacer(Modifier.size(16.dp))

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.debt_calculation),
                color = AppColors.textPrimary,
                modifier = Modifier.padding(8.dp)
            )

            ThemeOption(
                text = stringResource(R.string.auto_delete_paid_debts),
                selected = debtAutoDelete,
                onClick = {
                    settingsViewModel.debtAutoDelete(true)
                }
            )

            ThemeOption(
                text = stringResource(R.string.auto_calculate_overpayments),
                selected = debtAutoCount,
                onClick = {
                    settingsViewModel.debtAutoCount(true)
                }
            )

            ThemeOption(
                text = stringResource(R.string.nothing),
                selected = !debtAutoCount && !debtAutoDelete,
                onClick = {
                    settingsViewModel.debtAutoCount(false)
                    settingsViewModel.debtAutoDelete(false)
                }
            )
        }
    }
}

@Composable
fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DebtRadioButton(
            selected = selected,
            text = text,
            onClick = onClick,
        )
    }
}