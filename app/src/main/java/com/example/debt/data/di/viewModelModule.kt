package com.example.debt.data.di


import com.example.debt.ui.screens.main.MainDebtorViewModel
import com.example.debt.ui.screens.settings.AppSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainDebtorViewModel(get()) }
    viewModel { AppSettingsViewModel() }
}