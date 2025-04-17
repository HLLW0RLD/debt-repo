package com.example.debt.app.data.di

import com.example.debt.app.data.db.AppDatabase
import com.example.debt.app.data.repo.LocalRepository
import com.example.debt.app.data.repo.LocalRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<LocalRepository> {
        val db = get<AppDatabase>()

        LocalRepositoryImpl(
            debtorDao = db.debtorDao()
        )
    }
}