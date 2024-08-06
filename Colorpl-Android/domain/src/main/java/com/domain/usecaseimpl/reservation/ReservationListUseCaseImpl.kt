package com.domain.usecaseimpl.reservation

import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationListUseCase
import com.domain.usecase.ReservationUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReservationListUseCaseImpl @Inject constructor(
    private val reservationRepository: ReservationRepository
) : ReservationListUseCase {
    override suspend fun invoke(): Flow<DomainResult<List<ReservationInfo>>> = flow {
        reservationRepository.getReservationAllShows().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val reservationListInfo = result.data.toEntity()
                    emit(DomainResult.success(reservationListInfo))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }

            }
        }
    }
}