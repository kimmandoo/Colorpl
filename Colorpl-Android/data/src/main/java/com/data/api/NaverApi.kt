package com.data.api

import com.data.model.paging.ResponsePagedFeed
import com.data.model.response.ResponseGeoCoding
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApi {
    @GET("geocode")
    suspend fun getLatLng(
        @Query("query") query: String,
    ): ResponseGeoCoding
}