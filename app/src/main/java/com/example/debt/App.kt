package com.example.debt

import android.app.Application
import com.example.debt.app.data.di.localDbModule
import com.example.debt.app.data.di.repositoryModule
import com.example.debt.data.di.viewModelModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@App.baseContext)
            modules(
                viewModelModule,
                repositoryModule,
                localDbModule
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