package com.data.repository

import androidx.paging.PagingData
import com.data.model.paging.reservation.Show
import com.data.model.response.ResponseShowSchedules
import com.data.model.response.ResponseShowSeat
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    suspend fun getReservationAllShows(filters: Map<String, String?>): Flow<PagingData<Show>>

    suspend fun getReservationShows(showDetailId: Int): Flow<ApiResult<Show>>

    suspend fun getReservationSchedule(showDetailId: Int, date: String): Flow<ApiResult<List<ResponseShowSchedules>>>

    suspend fun getReservationSeat(showDetailId: Int, showScheduleId: Int): Flow<ApiResult<Map<String, ResponseShowSeat>>>
}