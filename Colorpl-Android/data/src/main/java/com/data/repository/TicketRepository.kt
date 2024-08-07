package com.data.repository

import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseTicket
import com.data.model.response.ResponseTicketCreate
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketRepository {
    suspend fun createTicket(
        ticket: File,
        request: RequestTicketCreate
    ): Flow<ApiResult<ResponseTicketCreate>>
    suspend fun getAllTicket(): Flow<ApiResult<List<ResponseTicket>>>
    suspend fun getMonthlyTicket(date: String): Flow<ApiResult<List<ResponseTicket>>>
}