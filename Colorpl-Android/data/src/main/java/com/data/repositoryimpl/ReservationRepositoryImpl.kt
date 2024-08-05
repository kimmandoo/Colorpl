package com.data.repositoryimpl

import com.data.datasource.remote.ReservationDataSource
import com.data.model.response.ReservationInfo
import com.data.repository.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationDataSource: ReservationDataSource
) : ReservationRepository {
    override suspend fun getReservationShows(showsId: Int): ReservationInfo {

        return reservationDataSource.getReservationShow(showsId)
    }
}