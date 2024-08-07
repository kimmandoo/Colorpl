package com.data.datasource.remote

import com.data.model.response.ReservationInfo
import retrofit2.http.QueryMap

interface ReservationDataSource {
    suspend fun getReservationShow(
        showsId: Int,
    ): ReservationInfo

    suspend fun getReservationListShows(filters: Map<String, String>): List<ReservationInfo>
}