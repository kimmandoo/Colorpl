package com.data.datasource.remote

import com.data.model.paging.reservation.ResponsePagedShow
import com.data.model.paging.reservation.Show
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat

interface ReservationDataSource {
    suspend fun getReservationListShows(filters: Map<String, String?>): List<Show>

    suspend fun getReservationShow(
        showDetailId: Int,
    ): Show

    suspend fun getReservationSchedule(
        showDetailId: Int,
        date: String,
    ): List<ResponseShowSchedules>

    suspend fun getReservationSeat(
        showDetailId: Int,
        showScheduleId: Int,
    ): Map<String, ResponseShowSeat>

}