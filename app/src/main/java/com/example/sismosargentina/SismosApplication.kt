package com.example.sismosargentina

import android.app.Application
import com.example.sismosargentina.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SismosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SismosApplication)
            modules(appModule)
        }
    }
}
