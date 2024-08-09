package com.data.datasource.remote

import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseShowSchedules
import retrofit2.http.QueryMap

interface ReservationDataSource {
    suspend fun getReservationListShows(filters: Map<String, String>): List<ReservationInfo>

    suspend fun getReservationShow(
        showsId: Int,
    ): ReservationInfo

    suspend fun getReservationSchedule(
        showsId: Int,
        date: String,
    ): List<ResponseShowSchedules>

}