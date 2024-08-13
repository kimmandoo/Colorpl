package com.data.api

import com.data.model.response.ResponseReservationInfo
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ReservationApi {

    @GET("shows")
    suspend fun getReservationListShows(@QueryMap filters: Map<String, String?>): List<ResponseReservationInfo>

    @GET("shows/{showDetailId}")
    suspend fun getReservationShow(
        @Path("showDetailId") showDetailId: Int
    ): ResponseReservationInfo

    @GET("shows/{showDetailId}/schedules")
    suspend fun getReservationSchedule(
        @Path("showDetailId") showDetailId: Int,
        @Query("date") date: String,
    ): List<ResponseShowSchedules>

    @GET("shows/{showDetailId}/schedules/{showScheduleId}")
    suspend fun getReservationSeat(
        @Path("showDetailId") showDetailId: Int,
        @Path("showScheduleId") showScheduleId: Int,
    ): Map<String, ResponseShowSeat>

}