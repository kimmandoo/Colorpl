package com.data.api

import com.data.model.paging.ResponsePagedFeed
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {

    @GET("feed")
    suspend fun getFeedData(
        @Query("page") page:Int,
        @Query("items") items: Int
    ): ResponsePagedFeed
}