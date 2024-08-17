package com.domain.model

data class TicketRequest(
    val seat: String,
    val dateTime: String,
    val name: String,
    val category: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
)
