package com.domain.usecase

import com.domain.model.ReservationInfo
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ReservationUseCase {
    suspend operator fun invoke(showsId: Int): Flow<DomainResult<ReservationInfo>>
//    suspend fun getReservationInfo(showsId: Int): ReservationInfo
}