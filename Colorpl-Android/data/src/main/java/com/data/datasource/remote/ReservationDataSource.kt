package com.data.datasource.remote

import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseShowSchedules

interface ReservationDataSource {
    suspend fun getReservationListShows(filters: Map<String, String>): List<ReservationInfo>

    suspend fun getReservationShow(
        showDetailId: Int,
    ): ReservationInfo

    suspend fun getReservationSchedule(
        showDetailId: Int,
        date: String,
    ): List<ResponseShowSchedules>

    suspend fun getReservationSeat(
        showDetailId: Int,
        showScheduleId: Int,
    ): Map<String, Boolean>

}