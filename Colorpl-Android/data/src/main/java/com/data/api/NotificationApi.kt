package com.data.api

import com.data.model.response.ResponseMarkerData
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {

    @GET("markers")
    suspend fun getMarkers(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("radius") radius : Double
    ) : ResponseMarkerData
}