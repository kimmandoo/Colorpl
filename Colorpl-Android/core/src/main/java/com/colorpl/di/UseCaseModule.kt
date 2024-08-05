package com.colorpl.di

import com.domain.usecase.CommentUseCase
import com.domain.usecase.FeedUseCase
import com.domain.usecase.GeocodingUseCase
import com.domain.usecase.NotificationUseCase
import com.domain.usecase.OpenAiUseCase
import com.domain.usecase.TicketCreateUseCase
import com.domain.usecase.TmapRouteUseCase
import com.domain.usecaseimpl.feed.CommentUseCaseImpl
import com.domain.usecaseimpl.feed.FeedUseCaseImpl
import com.domain.usecaseimpl.naver.GeocodingUesCaseImpl
import com.domain.usecaseimpl.notification.NotificationUseCaseImpl
import com.domain.usecaseimpl.openai.OpenAiUseCaseImpl
import com.domain.usecaseimpl.ticket.TicketCreateUseCaseImpl
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

    @Binds
    @Singleton
    fun provideFeedUseCase(
        feedUseCaseImpl: FeedUseCaseImpl
    ): FeedUseCase

    @Binds
    @Singleton
    fun provideCommentUseCase(
        commentUseCaseImpl: CommentUseCaseImpl
    ): CommentUseCase

    @Binds
    @Singleton
    fun provideTicketCreateUseCase(
        ticketCreateUseCaseImpl: TicketCreateUseCaseImpl
    ): TicketCreateUseCase

    @Binds
    @Singleton
    fun bindGeocodingUseCase(
        geocodingUesCaseImpl: GeocodingUesCaseImpl
    ): GeocodingUseCase
}