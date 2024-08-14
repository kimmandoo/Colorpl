package com.domain.model

import java.io.Serializable

data class TicketResponse(
    val id: Int,
    val seat: String,
    val dateTime: String,
    val name: String,
    val category: String,
    val location: String?,
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val imgUrl: String?,
    val reviewExists: Boolean,
    val reviewId: Int,
    val type: String = "CUSTOM"
) : Serializable
