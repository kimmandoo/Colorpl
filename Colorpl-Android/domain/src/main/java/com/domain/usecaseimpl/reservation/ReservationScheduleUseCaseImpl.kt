package com.domain.usecaseimpl.reservation

import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.ReservationPairInfo
import com.domain.usecase.ReservationScheduleUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ReservationScheduleUseCaseImpl @Inject constructor(
    private val reservationRepository: ReservationRepository
) : ReservationScheduleUseCase {

    override suspend fun invoke(
        showDetailId: Int,
        date: String
    ): Flow<DomainResult<List<ReservationPairInfo>>> = flow {
        reservationRepository.getReservationSchedule(
            showDetailId = showDetailId,
            date = date
        ).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val reservationPairInfoList = result.data.toEntity()
                    emit(DomainResult.success(reservationPairInfoList))
                }
                is ApiResult.Error -> {
                    Timber.d("예약 에러 확인 ${result.exception}")
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }
}