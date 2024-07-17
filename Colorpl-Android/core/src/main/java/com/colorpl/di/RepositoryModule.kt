package com.colorpl.di

import com.data.repository.NotificationRepository
import com.data.repositoryimpl.NotificationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ) : NotificationRepository
}