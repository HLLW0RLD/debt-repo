package com.example.debt.app.data.di

import com.example.debt.data.db.AppDatabase
import org.koin.dsl.module

val localDbModule = module {
    single<AppDatabase> {
        AppDatabase.create(get())
    }
}