package com.example.debt.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debt.R
import com.example.debt.ui.items.DebtRadioButton
import com.example.debt.ui.theme.AppColors
import com.example.debt.ui.theme.ThemeMode
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSettingsScreen(
    onBackClick: () -> Unit
) {
    val viewModel: AppSettingsViewModel = koinViewModel()

    val debtAutoCount by viewModel.autoCountDebts.collectAsState()
    val debtAutoDelete by viewModel.debtAutoDelete.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()

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
            IconButton(onClick = onBackClick) {
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
                    viewModel.setTheme(ThemeMode.SYSTEM)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_light),
                selected = selectedTheme == ThemeMode.LIGHT,
                onClick = {
                    viewModel.setTheme(ThemeMode.LIGHT)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_dark),
                selected = selectedTheme == ThemeMode.DARK,
                onClick = {
                    viewModel.setTheme(ThemeMode.DARK)
                }
            )

            ThemeOption(
                text = stringResource(R.string.theme_colored),
                selected = selectedTheme == ThemeMode.COLOR,
                onClick = {
                    viewModel.setTheme(ThemeMode.COLOR)
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
                    viewModel.debtAutoDelete(true)
                }
            )

            ThemeOption(
                text = stringResource(R.string.auto_calculate_overpayments),
                selected = debtAutoCount,
                onClick = {
                    viewModel.debtAutoCount(true)
                }
            )

            ThemeOption(
                text = stringResource(R.string.nothing),
                selected = !debtAutoCount && !debtAutoDelete,
                onClick = {
                    viewModel.debtAutoCount(false)
                    viewModel.debtAutoDelete(false)
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DebtRadioButton(
            selected = selected,
            text = text,
            onClick = onClick,
        )
    }
}