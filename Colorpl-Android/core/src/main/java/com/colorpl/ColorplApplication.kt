package com.colorpl

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ColorplApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}