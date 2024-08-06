package com.data.api

import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseReservationShows
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationApi {

    @GET("shows/{id}")
    suspend fun getReservationShow(
        @Path("id") id: Int
    ): ReservationInfo

    @GET("shows/")
    suspend fun getReservationAllShows(): List<ReservationInfo>

}