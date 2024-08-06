package com.colorpl.di

import com.data.repository.CommentRepository
import com.data.repository.FeedRepository
import com.data.repository.GeocodingRepository
import com.data.repository.MemberRepository
import com.data.repository.NotificationRepository
import com.data.repository.OpenAiRepository
import com.data.repository.ReviewRepository
import com.data.repository.ReservationRepository
import com.data.repository.SignRepository
import com.data.repository.TicketRepository
import com.data.repository.TmapRouteRepository
import com.data.repository.TokenRepository
import com.data.repositoryimpl.CommentRepositoryImpl
import com.data.repositoryimpl.FeedRepositoryImpl
import com.data.repositoryimpl.GeocodingRepositoryImpl
import com.data.repositoryimpl.MemberRepositoryImpl
import com.data.repositoryimpl.NotificationRepositoryImpl
import com.data.repositoryimpl.OpenAiRepositoryImpl
import com.data.repositoryimpl.ReviewRepositoryImpl
import com.data.repositoryimpl.ReservationRepositoryImpl
import com.data.repositoryimpl.SignRepositoryImpl
import com.data.repositoryimpl.TicketRepositoryImpl
import com.data.repositoryimpl.TmapRouteRepositoryImpl
import com.data.repositoryimpl.TokenRepositoryImpl
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
    fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Singleton
    @Binds
    fun bindsTmapRouteRepository(
        tmapRouteRepositoryImpl: TmapRouteRepositoryImpl
    ): TmapRouteRepository

    @Singleton
    @Binds
    fun bindsOpenAiRepository(
        openAiRepositoryImpl: OpenAiRepositoryImpl
    ): OpenAiRepository

    @Singleton
    @Binds
    fun bindsFeedPagingRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository

    @Singleton
    @Binds
    fun bindsSignRepository(
        signRepositoryImpl: SignRepositoryImpl
    ): SignRepository

    @Singleton
    @Binds
    fun bindsTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Singleton
    @Binds
    fun bindsCommentPagingRepository(
        commentPagingRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Singleton
    @Binds
    fun bindsTicketRepository(
        ticketRepositoryImpl: TicketRepositoryImpl
    ): TicketRepository

    @Singleton
    @Binds
    fun bindsReservationRepository(
        reservationRepositoryImpl: ReservationRepositoryImpl
    ): ReservationRepository


    @Singleton
    @Binds
    fun bindsMemberRepository(
        memberRepositoryImpl: MemberRepositoryImpl
    ): MemberRepository

    @Singleton
    @Binds
    fun bindsGeocodingRepository(
        geocodingRepositoryImpl: GeocodingRepositoryImpl
    ): GeocodingRepository

    @Singleton
    @Binds
    fun bindsReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}