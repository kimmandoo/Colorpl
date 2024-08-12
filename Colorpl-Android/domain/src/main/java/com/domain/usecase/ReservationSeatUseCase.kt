package com.domain.usecase

import com.domain.model.Seat
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ReservationSeatUseCase {

    suspend operator fun invoke(showDetailId: Int, showScheduleId: Int): Flow<DomainResult<List<Seat>>>

}