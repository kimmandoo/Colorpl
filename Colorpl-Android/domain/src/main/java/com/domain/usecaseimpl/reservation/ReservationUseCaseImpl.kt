package com.domain.usecaseimpl.reservation

import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(
    private val reservationRepository: ReservationRepository
) : ReservationUseCase {
    override suspend fun invoke(
        showsId: Int
    ): Flow<DomainResult<ReservationInfo>> = flow {
            reservationRepository.getReservationShows(showsId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val reservationInfo = result.data.toEntity()
                        emit(DomainResult.success(reservationInfo))
                    }
                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }

                }
        }
    }


    //override suspend fun getReservationInfo(showId: Int): ReservationInfo {
    //        return reservationRepository.getReservationShows(showId).toEntity()
    //    }

}