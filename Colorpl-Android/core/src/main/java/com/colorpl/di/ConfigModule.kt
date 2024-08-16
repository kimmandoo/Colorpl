package com.colorpl.di

import com.colorpl.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    @Named("bootpay")
    fun provideBootpayKey(): String = BuildConfig.BOOT_PAY_KEY
}