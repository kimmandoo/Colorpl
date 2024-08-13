package com.data.datasourceimpl

import com.data.api.ReservationApi
import com.data.datasource.remote.ReservationDataSource
import com.data.model.paging.reservation.ResponsePagedShow
import com.data.model.paging.reservation.Show
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat
import timber.log.Timber
import javax.inject.Inject

class ReservationDataSourceImpl @Inject constructor(
    private val reservationApi: ReservationApi
) : ReservationDataSource {

    override suspend fun getReservationListShows(filters: Map<String, String?>): List<Show> {
        Timber.tag("showDataSourceImplTag").d(filters.toString())
        return reservationApi.getReservationListShows(filters)
    }

    override suspend fun getReservationShow(showDetailId: Int): Show {
        return reservationApi.getReservationShow(showDetailId)
    }

    override suspend fun getReservationSchedule(showDetailId: Int, date: String): List<ResponseShowSchedules> {
        return reservationApi.getReservationSchedule(showDetailId, date)
    }
    override suspend fun getReservationSeat(showDetailId: Int, showScheduleId: Int): Map<String, ResponseShowSeat> {
        return reservationApi.getReservationSeat(showDetailId, showScheduleId)
    }
}
