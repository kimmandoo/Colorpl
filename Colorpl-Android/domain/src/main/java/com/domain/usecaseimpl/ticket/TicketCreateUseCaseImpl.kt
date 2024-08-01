package com.domain.usecaseimpl.ticket

import com.data.model.request.RequestTicketCreate
import com.data.repository.TicketRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Ticket
import com.domain.usecase.TicketCreateUseCase
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class TicketCreateUseCaseImpl @Inject constructor(
    private val ticketRepository: TicketRepository,
) : TicketCreateUseCase {
    override suspend fun invoke(image: File, ticket: Ticket): Flow<RepoResult<String>> = flow {
        ticketRepository.createTicket(
            image, RequestTicketCreate(
                name = ticket.name,
                theater = ticket.theater,
                dateTime = ticket.date,
                seat = ticket.seat,
                category = ticket.category
            )
        ).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val description = result.data
                    Timber.d("$description")
                    emit(RepoResult.success(description))
                }

                is ApiResult.Error -> {
                    emit(RepoResult.error(result.exception))
                }
            }
        }
    }
}