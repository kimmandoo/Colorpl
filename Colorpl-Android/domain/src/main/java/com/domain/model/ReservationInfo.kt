package com.domain.model

import java.io.Serializable

data class ReservationInfo (
    val reservationInfoId: Int = 0,
    val contentImg: String? = null,
    val title: String = "",
    val cast: String = "",
    val category: String? = null,
    val runtime: String = "",
    val price: String = "",
) : Serializable