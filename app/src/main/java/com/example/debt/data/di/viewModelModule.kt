package com.example.debt.data.di


import com.example.debt.app.ui.screens.DebtorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DebtorViewModel(get()) }
}