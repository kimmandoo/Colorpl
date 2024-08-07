package com.domain.model

data class Ticket(
    val name: String,
    val location: String,
    val dateTime: String,
    val seat: String = "",
    val category: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imgUrl: String = ""
)
