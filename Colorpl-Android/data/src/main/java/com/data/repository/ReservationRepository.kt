package com.data.repository

import com.data.model.response.ResponseReservationInfo
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    suspend fun getReservationAllShows(filters: Map<String, String>): Flow<ApiResult<List<ResponseReservationInfo>>>

    suspend fun getReservationShows(showDetailId: Int): Flow<ApiResult<ResponseReservationInfo>>

    suspend fun getReservationSchedule(showDetailId: Int, date: String): Flow<ApiResult<List<ResponseShowSchedules>>>

    suspend fun getReservationSeat(showDetailId: Int, showScheduleId: Int): Flow<ApiResult<Map<String, ResponseShowSeat>>>
}