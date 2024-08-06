package com.colorpl.di

import com.domain.usecase.CommentUseCase
import com.domain.usecase.FeedUseCase
import com.domain.usecase.FeedUserUseCase
import com.domain.usecase.GeocodingUseCase
import com.domain.usecase.NotificationUseCase
import com.domain.usecase.OpenAiUseCase
import com.domain.usecase.ReservationListUseCase
import com.domain.usecase.ReviewCreateUseCase
import com.domain.usecase.ReservationUseCase
import com.domain.usecase.ReviewDeleteUseCase
import com.domain.usecase.ReviewEditUseCase
import com.domain.usecase.TicketCreateUseCase
import com.domain.usecase.TmapRouteUseCase
import com.domain.usecaseimpl.feed.CommentUseCaseImpl
import com.domain.usecaseimpl.feed.FeedUseCaseImpl
import com.domain.usecaseimpl.feed.FeedUserUseCaseImpl
import com.domain.usecaseimpl.naver.GeocodingUesCaseImpl
import com.domain.usecaseimpl.notification.NotificationUseCaseImpl
import com.domain.usecaseimpl.openai.OpenAiUseCaseImpl
import com.domain.usecaseimpl.reservation.ReservationListUseCaseImpl
import com.domain.usecaseimpl.reservation.ReservationUseCaseImpl
import com.domain.usecaseimpl.review.ReviewCreateUseCaseImpl
import com.domain.usecaseimpl.review.ReviewDeleteUseCaseImpl
import com.domain.usecaseimpl.review.ReviewEditUseCaseImpl
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
    fun bindsNotificationUseCase(
        notificationUseCaseImpl: NotificationUseCaseImpl
    ): NotificationUseCase

    @Binds
    @Singleton
    fun bindsTmapRouteUseCase(
        tmapRouteUseCaseImpl: TmapRouteUseCaseImpl
    ): TmapRouteUseCase

    @Binds
    @Singleton
    fun bindsOpenAiUseCase(
        openAiUseCaseImpl: OpenAiUseCaseImpl
    ): OpenAiUseCase

    @Binds
    @Singleton
    fun bindsFeedUseCase(
        feedUseCaseImpl: FeedUseCaseImpl
    ): FeedUseCase

    @Binds
    @Singleton
    fun bindsFeedUserUseCase(
        feedUserUseCaseImpl: FeedUserUseCaseImpl
    ): FeedUserUseCase

    @Binds
    @Singleton
    fun bindsCommentUseCase(
        commentUseCaseImpl: CommentUseCaseImpl
    ): CommentUseCase

    @Binds
    @Singleton
    fun bindsTicketCreateUseCase(
        ticketCreateUseCaseImpl: TicketCreateUseCaseImpl
    ): TicketCreateUseCase

    @Binds
    @Singleton
    fun bindsReservationUseCase(
        reservationUseCaseImpl: ReservationUseCaseImpl
    ): ReservationUseCase

    @Binds
    @Singleton
    fun bindsReservationListUseCase(
        reservationListUseCaseImpl: ReservationListUseCaseImpl
    ): ReservationListUseCase


    @Binds
    @Singleton
    fun bindsGeocodingUseCase(
        geocodingUesCaseImpl: GeocodingUesCaseImpl
    ): GeocodingUseCase

    @Binds
    @Singleton
    fun bindsReviewCreateUseCase(
        reviewCreateUseCaseImpl: ReviewCreateUseCaseImpl
    ): ReviewCreateUseCase

    @Binds
    @Singleton
    fun bindsReviewDeleteUseCase(
        reviewDeleteUseCaseImpl: ReviewDeleteUseCaseImpl
    ): ReviewDeleteUseCase

    @Binds
    @Singleton
    fun bindsReviewEditUseCase(
        reviewEditUseCaseImpl: ReviewEditUseCaseImpl
    ): ReviewEditUseCase
}