package com.domain.usecase

import com.domain.model.Ticket
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TicketCreateUseCase {
    suspend operator fun invoke(
        image: File,
        ticket: Ticket
    ): Flow<DomainResult<Int>>
}