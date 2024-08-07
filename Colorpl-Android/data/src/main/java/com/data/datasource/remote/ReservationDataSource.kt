package com.data.datasource.remote

import com.data.api.ReservationApi
import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseReservationShows
import javax.inject.Inject

interface ReservationDataSource {
    suspend fun getReservationShow(
        showsId: Int,
    ): ReservationInfo

    suspend fun getReservationAllShow(): List<ReservationInfo>
}