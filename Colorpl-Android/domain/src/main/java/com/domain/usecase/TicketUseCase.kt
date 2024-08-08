package com.domain.usecase

import com.domain.model.TicketRequest
import com.domain.model.TicketResponse
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketUseCase {
    fun createTicket(
        image: File,
        ticket: TicketRequest
    ): Flow<DomainResult<Int>>

    fun createReservationTicket(
        image: File,
        ticket: Int
    ): Flow<DomainResult<Int>>

    suspend fun getAllTicket(): Flow<DomainResult<List<TicketResponse>>>

    suspend fun getMonthlyTicket(
        date: String
    ): Flow<DomainResult<List<TicketResponse>>>

}