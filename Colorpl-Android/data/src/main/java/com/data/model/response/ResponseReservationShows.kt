package com.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ResponseReservationShows(
    val items: List<ReservationInfo>
)

@Serializable
data class ReservationInfo(
    @SerialName("id") val id: Int,
    @SerialName("apiId") val apiId: String,
    @SerialName("name") val name: String,
    @SerialName("cast") val cast: String,
    @SerialName("runtime") val runtime: String,
    @SerialName("priceBySeatClass") val priceBySeatClass: PriceBySeatClass,
    @SerialName("posterImagePath") val posterImagePath: String,
    @SerialName("area") val area: String,
    @SerialName("category") val category: String?,
    @SerialName("state") val state: String,
    @SerialName("seats") val seats: List<Seat>,
    @SerialName("hall") val hall: String,
)

data class PriceBySeatClass(
    val gradeR: Int? = -1,
    val gradeS: Int? = -1,
    val gradeA: Int? = -1,
    val gradeB: Int? = -1,
)

data class Seat(
    val row: Int,
    val col: Int,
    val grade: String,
)