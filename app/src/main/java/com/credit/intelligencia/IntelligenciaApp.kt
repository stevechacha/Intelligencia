package com.credit.intelligencia

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.BuildConfig
import com.credit.intelligencia.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class IntelligenciaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@IntelligenciaApp)
            modules(
                apiModule,
                environmentModule,
                networkModule,
                loginModule,
                registerModule,
                permissionModule,
                loanApplicationModule,
                loanDisbursmentModule,
                authModule,
                welcomeModule,
                splashModule
            )
        }
    }
}