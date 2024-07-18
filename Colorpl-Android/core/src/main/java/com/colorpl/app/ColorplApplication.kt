package com.colorpl.app

import android.app.Application
import com.colorpl.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.naver.maps.map.NaverMapSdk

@HiltAndroidApp
class ColorplApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // NaverMapSdk
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT_ID)
    }
}