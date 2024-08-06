package com.data.datasourceimpl

import com.data.api.ReservationApi
import com.data.datasource.remote.ReservationDataSource
import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseReservationShows
import javax.inject.Inject

class ReservationDataSourceImpl @Inject constructor(
    private val reservationApi: ReservationApi
) : ReservationDataSource {
    override suspend fun getReservationShow(showId: Int): ReservationInfo {
        return reservationApi.getReservationShow(showId)
    }

    override suspend fun getReservationAllShow(): List<ReservationInfo> {
        return reservationApi.getReservationAllShows()
    }
}