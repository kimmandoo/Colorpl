package com.data.model.response


data class ResponseTicket(
    val id: Int,
    val seat: String,
    val dateTime: String,
    val name: String,
    val category: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val imgUrl: String
)