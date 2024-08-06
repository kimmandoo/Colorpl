package com.data.api

import com.data.model.paging.ResponsePagedComment
import com.data.model.paging.ResponsePagedFeed
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("reviews/members/{memberId}/all")
    suspend fun getAllFeedData(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    @Multipart
    @POST("reviews/members/{memberId}/tickets/{ticketId}")
    suspend fun createUserFeedData(
        @Path("memberId") memberId: Int,
        @Path("ticketId") ticketId: Int,
        @Part file: MultipartBody.Part?,
        @Part("request") request: RequestBody,
    ): ResponseReviewCreate

    @GET("reviews/members/{memberId}")
    suspend fun getUserFeedData(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    @GET("reviews/details/{reviewId}")
    suspend fun getDetailFeedData(
        @Path("reviewId") reviewId: Int,
        @Query("memberId") memberId: Int,
    ): ResponsePagedFeed

    @PUT("reviews/members/{memberId}/reviews/{reviewId}")
    suspend fun editUserFeedData(
        @Path("memberId") memberId: Int,
        @Path("reviewId") reviewId: Int,
        @Body requestReviewEdit: RequestReviewEdit
    ): ResponseReviewEdit

    @DELETE("reviews/{reviewId}")
    suspend fun deleteFeedData(
        @Path("reviewId") reviewId: Int,
    ): ResponseReviewEdit

    @PUT("comments/{commentId}/members/{memberId}")
    suspend fun editCommentData(
        @Path("commentId") commentId: Int,
        @Path("memberId") memberId: Int,
    ): ResponsePagedComment

    @POST("comments/reviews/{reviewId}/members/{memberId}")
    suspend fun createCommentData(
        @Path("reviewId") reviewId: Int,
        @Path("memberId") memberId: Int,
    ): ResponsePagedComment

    @GET("comments/reviews/{reviewId}")
    suspend fun getCommentData(
        @Path("reviewId") reviewId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedComment

    @GET("comments/{commentId}")
    suspend fun deleteCommentData(
        @Path("commentId") commentId: Int,
    ): ResponsePagedComment
}