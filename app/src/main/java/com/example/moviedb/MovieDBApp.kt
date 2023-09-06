package com.example.moviedb

import android.app.Application
import com.example.moviedb.core.di.coreModule
import com.example.moviedb.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieDBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MovieDBApp)
            androidFileProperties()
            modules(coreModule, mainModule)
        }
    }
}