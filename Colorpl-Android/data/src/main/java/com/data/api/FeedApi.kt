package com.data.api

import com.data.model.paging.ResponsePagedComment
import com.data.model.paging.ResponsePagedFeed
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("reviews")
    suspend fun getFeedData(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    @GET("comments/reviews/{reviewId}")
    suspend fun getCommentData(
        @Path("reviewId") reviewId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedComment
}