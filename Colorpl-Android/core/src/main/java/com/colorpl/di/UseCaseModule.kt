package com.colorpl.di

import com.domain.usecase.NotificationUseCase
import com.domain.usecaseimpl.notification.NotificationUseCaseImpl
import com.google.android.datatransport.runtime.dagger.Component
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    @Singleton
    fun provideNotificationUseCase(
        notificationUseCaseImpl: NotificationUseCaseImpl
    ) : NotificationUseCase


}