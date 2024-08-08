package com.domain.usecaseimpl.ticket

import com.data.model.request.RequestReservationTicketCreate
import com.data.repository.TicketRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.TicketRequest
import com.domain.model.TicketResponse
import com.domain.usecase.TicketUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class TicketUseCaseImpl @Inject constructor(
    private val ticketRepository: TicketRepository,
) : TicketUseCase {
    override fun createTicket(image: File, ticket: TicketRequest): Flow<DomainResult<Int>> = flow {
        Timber.d("$ticket")
        ticketRepository.createTicket(
            image, ticket.toEntity()
        ).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val description = result.data
                    Timber.d("$description")
                    emit(DomainResult.success(description))
                }

                is ApiResult.Error -> {
                    Timber.d("${result.exception}")
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }

    override fun createReservationTicket(
        image: File,
        ticket: Int
    ): Flow<DomainResult<Int>> = flow {
        ticketRepository.createReservationTicket(image, RequestReservationTicketCreate(ticket))
            .collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }

                    is ApiResult.Success -> {
                        val response = result.data
                        Timber.d("$response")
                        emit(DomainResult.success(response))
                    }
                }
            }
    }

    override suspend fun getAllTicket(): Flow<DomainResult<List<TicketResponse>>> = flow {
        ticketRepository.getAllTicket().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val response = result.data.map { it.toEntity() }
                    Timber.d("$response")
                    emit(DomainResult.success(response))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }

    override suspend fun getMonthlyTicket(date: String): Flow<DomainResult<List<TicketResponse>>> =
        flow {
            ticketRepository.getMonthlyTicket(date).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val response = result.data.map { it.toEntity() }
                        Timber.d("$response")
                        emit(DomainResult.success(response))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }
                }
            }
        }
}