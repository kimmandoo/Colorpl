package com.domain.usecaseimpl.reservation

import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Seat
import com.domain.usecase.ReservationSeatUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ReservationSeatUseCaseImpl @Inject constructor(
    private val reservationRepository: ReservationRepository
): ReservationSeatUseCase {

    override suspend fun invoke(
        showDetailId: Int,
        showScheduleId: Int
    ): Flow<DomainResult<List<Seat>>> =flow {
        reservationRepository.getReservationSeat(
            showDetailId,
            showScheduleId
        ).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    Timber.tag("Seat api 요청").d("${result.data}")
                    val seatList = result.data.toEntity(10, 16)
                    emit(DomainResult.success(seatList))
                }
                is ApiResult.Error -> {
                    Timber.tag("Seat api 요청").e("실패")
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }
}