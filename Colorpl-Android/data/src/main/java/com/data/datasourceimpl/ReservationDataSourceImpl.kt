package com.data.datasourceimpl

import com.data.api.ReservationApi
import com.data.datasource.remote.ReservationDataSource
import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat
import javax.inject.Inject

class ReservationDataSourceImpl @Inject constructor(
    private val reservationApi: ReservationApi
) : ReservationDataSource {

    override suspend fun getReservationListShows(filters: Map<String, String>): List<ReservationInfo> {
        return reservationApi.getReservationListShows(filters)
    }

    override suspend fun getReservationShow(showDetailId: Int): ReservationInfo {
        return reservationApi.getReservationShow(showDetailId)
    }

    override suspend fun getReservationSchedule(showDetailId: Int, date: String): List<ResponseShowSchedules> {
        return reservationApi.getReservationSchedule(showDetailId, date)
    }
    override suspend fun getReservationSeat(showDetailId: Int, showScheduleId: Int): Map<String, ResponseShowSeat> {
        return reservationApi.getReservationSeat(showDetailId, showScheduleId)
    }
}
