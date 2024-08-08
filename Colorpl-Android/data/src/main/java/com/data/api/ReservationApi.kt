package com.data.api

import com.data.model.response.ReservationInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ReservationApi {

    @GET("shows/{id}")
    suspend fun getReservationShow(
        @Path("id") id: Int
    ): ReservationInfo

    @GET("shows")
    suspend fun getReservationListShows(@QueryMap filters: Map<String, String>): List<ReservationInfo>

}