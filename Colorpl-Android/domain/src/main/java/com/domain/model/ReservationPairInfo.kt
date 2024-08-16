package com.domain.model

data class ReservationPairInfo(
    val placeName: String,
    val hallName: String,
    val hallCountSeat: Int,
    val timeTableList: List<TimeTable>,
)




