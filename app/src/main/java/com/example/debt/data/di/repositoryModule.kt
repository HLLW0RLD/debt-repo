package com.example.debt.app.data.di

import com.example.debt.app.data.repo.DebtRepository
import com.example.debt.app.data.repo.DebtRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<DebtRepository> {
        DebtRepositoryImpl(get())
    }
}