package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.ReservationDataSource
import com.data.model.response.ReservationInfo
import com.data.model.response.ResponseShowSchedules
import com.data.repository.ReservationRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationDataSource: ReservationDataSource
) : ReservationRepository {

    override suspend fun getReservationAllShows(filters: Map<String, String>): Flow<ApiResult<List<ReservationInfo>>> {
        return flow {
            emit(safeApiCall {
                reservationDataSource.getReservationListShows(filters)
            })
        }
    }

    override suspend fun getReservationShows(showsId: Int): Flow<ApiResult<ReservationInfo>> {
        return flow {
            emit(safeApiCall {
                reservationDataSource.getReservationShow(showsId)
            })
        }
    }

    override suspend fun getReservationSchedule(
        showDetailId: Int,
        date: String
    ): Flow<ApiResult<List<ResponseShowSchedules>>> {
        return flow {
            emit(safeApiCall {
                reservationDataSource.getReservationSchedule(showDetailId, date)
            })
        }
    }
}