package com.domain.usecaseimpl.reservation

import androidx.paging.PagingData
import androidx.paging.map
import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.ReservationInfo
import com.domain.usecase.ReservationListUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ReservationListUseCaseImpl @Inject constructor(
    private val reservationRepository: ReservationRepository
) : ReservationListUseCase {
    override suspend fun invoke(
        filters: Map<String, String?>
    ): Flow<PagingData<ReservationInfo>> {
        return reservationRepository.getReservationAllShows(filters)
            .map { pagingData ->
                Timber.tag("pagerUseCase").d("$pagingData")
                pagingData.map { show ->
                    show.toEntity()
                }
            }
    }
}