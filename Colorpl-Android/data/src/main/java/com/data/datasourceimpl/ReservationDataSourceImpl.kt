package com.data.datasourceimpl

import com.data.api.ReservationApi
import com.data.datasource.remote.ReservationDataSource
import com.data.model.response.ReservationInfo
import javax.inject.Inject

class ReservationDataSourceImpl @Inject constructor(
    private val reservationApi: ReservationApi
) : ReservationDataSource {
    override suspend fun getReservationShow(showId: Int): ReservationInfo {
        return reservationApi.getReservationShow(showId)
    }
}