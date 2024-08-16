package com.data.repository

import com.data.model.request.RequestReservationTicketCreate
import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseSingleTicket
import com.data.model.response.ResponseTicket
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketRepository {
    suspend fun createTicket(
        ticket: File,
        request: RequestTicketCreate
    ): Flow<ApiResult<Int>>

    suspend fun createReservationTicket(
        ticket: File,
        request: RequestReservationTicketCreate
    ): Flow<ApiResult<Int>>

    suspend fun getAllTicket(): Flow<ApiResult<List<ResponseTicket>>>
    suspend fun getMonthlyTicket(date: String): Flow<ApiResult<List<ResponseTicket>>>

    suspend fun getSingleTicket(id: Int): Flow<ApiResult<ResponseSingleTicket>>
    suspend fun deleteTicket(id: Int): Flow<ApiResult<Unit>>
    suspend fun putTicket(
        id: Int,
        ticket: File?,
        request: RequestTicketCreate
    ): Flow<ApiResult<Int>>
}