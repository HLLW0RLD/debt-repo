package com.example.debt.data.di


import com.example.debt.ui.screens.main.DebtorsFeedViewModel
import com.example.debt.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DebtorsFeedViewModel(get()) }
    viewModel { SettingsViewModel() }
}