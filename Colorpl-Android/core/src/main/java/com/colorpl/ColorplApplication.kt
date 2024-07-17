package com.colorpl

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ColorplApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workFactory : HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory = workFactory)
            .build()
}