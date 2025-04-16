package com.example.debtdiary

import android.app.Application
import com.example.debt.app.data.di.localDbModule
import com.example.debt.app.data.di.repositoryModule
import com.example.debtdiary.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App.baseContext)
            modules(
                localDbModule,
                repositoryModule,
                viewModelModule
            )
        }.koin

        _appInstance = this
    }

    companion object {
        private var _appInstance: App? = null
        val appInstance
            get() = _appInstance!!
    }
}