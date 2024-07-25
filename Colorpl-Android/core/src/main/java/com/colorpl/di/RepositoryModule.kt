package com.colorpl.di

import com.data.repository.NotificationRepository
import com.data.repository.TmapRouteRepository
import com.data.repositoryimpl.NotificationRepositoryImpl
import com.data.repositoryimpl.TmapRouteRepositoryImpl
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

    @Singleton
    @Binds
    fun provideTmapRouteRepository(
        tmapRouteRepositoryImpl: TmapRouteRepositoryImpl
    ) : TmapRouteRepository
}