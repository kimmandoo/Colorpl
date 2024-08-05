package com.domain.model

import java.io.Serializable

data class ReservationInfo(
    val reservationInfoId: Int,
    val contentImg: String?,
    val title: String,
    val category: String?,
    val runtime: String,
    val price: String,
) : Serializable