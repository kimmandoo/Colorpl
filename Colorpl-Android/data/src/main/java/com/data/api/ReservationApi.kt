package com.data.api

import com.data.model.response.ReservationInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationApi {

    @GET("shows/{id}")
    suspend fun getReservationShow(
        @Path("id") id: Int
    ): ReservationInfo
}