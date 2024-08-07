package com.domain.usecase

import com.domain.model.Ticket
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketUseCase {
    fun createTicket(
        image: File,
        ticket: Ticket
    ): Flow<DomainResult<Int>>

    suspend fun getAllTicket(): Flow<DomainResult<List<Ticket>>>

    suspend fun getMonthlyTicket(
        date: String
    ): Flow<DomainResult<List<Ticket>>>

}