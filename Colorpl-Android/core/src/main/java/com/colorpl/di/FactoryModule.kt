package com.colorpl.di

import com.data.api.FeedApi
import com.data.datasourceimpl.CommentPagingDataSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {
    @Provides
    @Singleton
    fun provideCommentPagingDataSourceFactory(api: FeedApi): CommentPagingDataSourceFactory {
        return CommentPagingDataSourceFactory(api)
    }
}