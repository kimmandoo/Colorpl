package com.data.model.response


data class ResponseTicket(
    val id: Int,
    val seat: String,
    val dateTime: String,
    val name: String,
    val category: String,
    val location: String?,
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val imgUrl: String,
    val reviewExists: Boolean,
    val reviewId: Int
)