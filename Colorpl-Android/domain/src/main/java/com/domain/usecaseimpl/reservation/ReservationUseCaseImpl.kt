package com.domain.usecaseimpl.reservation

import com.data.repository.ReservationRepository
import com.domain.mapper.toEntity
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationUseCase
import javax.inject.Inject

class ReservationUseCaseImpl @Inject constructor(private val reservationRepository: ReservationRepository) :
    ReservationUseCase {
    override suspend fun getReservationInfo(showId: Int): ReservationInfo {
        return reservationRepository.getReservationShows(showId).toEntity()
    }

}