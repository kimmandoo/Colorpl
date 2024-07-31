package com.domain.model

data class ReservationPairInfo(
    val reservationPlaceId: Int,
    val placeName: String,
    val theaterId: Int,
    val theaterName: String,
    val theaterTotalSeatCount: Int,
    val timeTableList: List<TimeTable>,
)




