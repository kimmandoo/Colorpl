package com.domain.usecase

import com.domain.model.ReservationPairInfo
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ReservationScheduleUseCase {

    suspend operator fun invoke(showDetailId: Int, date: String): Flow<DomainResult<List<ReservationPairInfo>>>

}