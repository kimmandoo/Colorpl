package com.domain.model

data class ReservationInfo(
    val reservationInfoId: Int,
    val contentImg: String?,
    val title: String,
    val category: String,
    val runtime: String,
    val price: String,
)
