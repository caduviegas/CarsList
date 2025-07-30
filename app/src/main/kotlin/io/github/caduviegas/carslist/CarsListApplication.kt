package io.github.caduviegas.carslist

import android.app.Application
import io.github.caduviegas.carslist.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CarsListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CarsListApplication)
            modules(appModules)
        }
    }
}