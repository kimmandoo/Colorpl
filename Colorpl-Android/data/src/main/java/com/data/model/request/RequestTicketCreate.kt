package com.data.model.request

data class RequestTicketCreate(
    val name: String,
    val theater: String,
    val dateTime: String,
    val seat: String,
    val category: String,
    val memberId: Int,
)