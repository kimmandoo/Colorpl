package com.domain.usecase

import com.domain.model.ReservationInfo

interface ReservationUseCase {
    suspend fun getReservationInfo(showsId: Int): ReservationInfo
}