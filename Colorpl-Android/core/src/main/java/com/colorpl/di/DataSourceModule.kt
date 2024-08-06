package com.colorpl.di

import com.data.datasource.CommentDataSource
import com.data.datasource.FeedDataSource
import com.data.datasource.local.TokenDataSource
import com.data.datasource.remote.GeocodingDataSource
import com.data.datasource.remote.MemberDataSource
import com.data.datasource.remote.NotificationDataSource
import com.data.datasource.remote.OpenAiDataSource
import com.data.datasource.remote.SignDataSource
import com.data.datasource.remote.TicketDataSource
import com.data.datasource.remote.TmapRouteDataSource
import com.data.datasourceimpl.CommentDataSourceImpl
import com.data.datasourceimpl.FeedDataSourceImpl
import com.data.datasourceimpl.GeocodingDataSourceImpl
import com.data.datasourceimpl.MemberDataSourceImpl
import com.data.datasourceimpl.NotificationDataSourceImpl
import com.data.datasourceimpl.OpenAiDataSourceImpl
import com.data.datasourceimpl.SignDataSourceImpl
import com.data.datasourceimpl.TicketDataSourceImpl
import com.data.datasourceimpl.TmapRouteDataSourceImpl
import com.data.datasourceimpl.TokenDataSourceImpl
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
    fun provideTokenDataSource(
        tokenDataSourceImpl: TokenDataSourceImpl
    ): TokenDataSource

    @Singleton
    @Binds
    fun bindGeoCodingDataSource(
        geocodingDataSourceImpl: GeocodingDataSourceImpl
    ): GeocodingDataSource


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

    @Singleton
    @Binds
    fun provideFeedDataSource(
        feedDataSourceImpl: FeedDataSourceImpl
    ): FeedDataSource

    @Singleton
    @Binds
    fun provideCommentDataSource(
        commentDataSourceImpl: CommentDataSourceImpl
    ): CommentDataSource

    @Singleton
    @Binds
    fun provideTicketDataSource(
        ticketDataSourceImpl: TicketDataSourceImpl
    ): TicketDataSource

    @Singleton
    @Binds
    fun bindsSignDataSource(
        signDataSourceImpl: SignDataSourceImpl
    ): SignDataSource

    @Singleton
    @Binds
    fun bindsMemberDataSource(
        memberDataSourceImpl: MemberDataSourceImpl
    ): MemberDataSource

}