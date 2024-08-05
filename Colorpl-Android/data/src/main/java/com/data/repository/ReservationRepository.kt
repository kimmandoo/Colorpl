package com.data.repository

import com.data.model.response.ReservationInfo

interface ReservationRepository {

    suspend fun getReservationShows(showsId: Int): ReservationInfo

}