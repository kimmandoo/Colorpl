package com.data.repository

import com.data.model.response.ReservationInfo
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {

    suspend fun getReservationShows(showsId: Int): Flow<ApiResult<ReservationInfo>>

}