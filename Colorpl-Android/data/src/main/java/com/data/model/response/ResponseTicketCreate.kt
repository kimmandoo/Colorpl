package com.data.model.response

data class ResponseTicketCreate(
    val file: String,
    val name: String,
    val theater: String,
    val date: String,
    val seat: String,
    val category: String
)