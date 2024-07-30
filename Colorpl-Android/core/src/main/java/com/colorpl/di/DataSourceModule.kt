package com.colorpl.di

import com.data.datasource.local.LocalDataSource
import com.data.datasource.local.LocalDataSourceImpl
import com.data.datasource.remote.NotificationDataSource
import com.data.datasource.remote.OpenAiDataSource
import com.data.datasource.remote.TmapRouteDataSource
import com.data.datasourceimpl.NotificationDataSourceImpl
import com.data.datasourceimpl.OpenAiDataSourceImpl
import com.data.datasourceimpl.TmapRouteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun provideLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource


    @Singleton
    @Binds
    fun provideNotificationDataSource(
        notificationDataSourceImpl: NotificationDataSourceImpl
    ): NotificationDataSource

    @Singleton
    @Binds
    fun provideTmapRouteDataSource(
        tmapRouteDataSourceImpl: TmapRouteDataSourceImpl
    ): TmapRouteDataSource

    @Singleton
    @Binds
    fun provideOpenAiDataSource(
        openAiDataSourceImpl: OpenAiDataSourceImpl
    ): OpenAiDataSource

}
