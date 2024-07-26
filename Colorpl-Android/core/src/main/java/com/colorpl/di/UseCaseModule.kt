package com.colorpl.di

import com.domain.usecase.NotificationUseCase
import com.domain.usecase.OpenAiUseCase
import com.domain.usecase.TmapRouteUseCase
import com.domain.usecaseimpl.notification.NotificationUseCaseImpl
import com.domain.usecaseimpl.openai.OpenAiUseCaseImpl
import com.domain.usecaseimpl.tmap.TmapRouteUseCaseImpl
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
    ): NotificationUseCase

    @Binds
    @Singleton
    fun provideTmapRouteUseCase(
        tmapRouteUseCaseImpl: TmapRouteUseCaseImpl
    ): TmapRouteUseCase

    @Binds
    @Singleton
    fun provideOpenAiUseCase(
        openAiUseCaseImpl: OpenAiUseCaseImpl
    ): OpenAiUseCase


}