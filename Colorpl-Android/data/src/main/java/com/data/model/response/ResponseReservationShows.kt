package com.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ResponseReservationShows(
    val items: List<ReservationInfo>
)


data class ReservationInfo(
    val id: Int,
    val apiId: String?,
    val name: String,
    val cast: String?,
    val runtime: String,
    val priceBySeatClass: Map<String, Int>,
    val posterImagePath: String,
    val area: String,
    val category: String?,
    val state: String?,
    val seats: List<Seat>?,
    val hall: String?,
)


data class PriceBySeatClass(
    val gradeR: Int?,
    val gradeS: Int?,
    val gradeA: Int?,
    val gradeB: Int?,
)

data class Seat(
    val row: Int,
    val col: Int,
    val grade: String,
)