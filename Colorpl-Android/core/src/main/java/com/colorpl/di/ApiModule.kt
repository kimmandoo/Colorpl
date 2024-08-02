package com.colorpl.di

import com.data.api.FeedApi
import com.data.api.NotificationApi
import com.data.api.OpenAiApi
import com.data.api.TicketApi
import com.data.api.SignApi
import com.data.api.TmapRouteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideNotificationApi(
        @NormalRetrofit
        retrofit: Retrofit
    ): NotificationApi = retrofit.create()

    @Singleton
    @Provides
    fun provideTmapApi(
        @TmapRetrofit
        retrofit: Retrofit
    ): TmapRouteApi = retrofit.create()

    @Singleton
    @Provides
    fun provideOpenAiApi(
        @GptRetrofit
        retrofit: Retrofit
    ): OpenAiApi = retrofit.create()

    @Singleton
    @Provides
    fun provideFeedApi(
        @NormalRetrofit
        retrofit: Retrofit
    ): FeedApi = retrofit.create()

    @Singleton
    @Provides
    fun provideTicketApi(
        @NormalRetrofit
        retrofit: Retrofit
    ): TicketApi = retrofit.create()

    @Singleton
    @Provides
    fun provideSignApi(
        @NormalRetrofit
        retrofit: Retrofit
    ): SignApi = retrofit.create()


}