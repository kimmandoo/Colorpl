package com.domain.usecaseimpl.ticket

import com.data.repository.TicketRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Ticket
import com.domain.usecase.TicketCreateUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class TicketCreateUseCaseImpl @Inject constructor(
    private val ticketRepository: TicketRepository,
) : TicketCreateUseCase {
    override suspend fun invoke(
        image: File,
        ticket: Ticket
    ): Flow<DomainResult<Int>> = flow {
        ticketRepository.createTicket(
            image, ticket.toEntity()
        ).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val description = result.data.toEntity()
                    Timber.d("$description")
                    emit(DomainResult.success(description))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }
}