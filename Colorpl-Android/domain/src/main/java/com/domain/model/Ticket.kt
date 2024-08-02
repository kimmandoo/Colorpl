package com.domain.model

data class Ticket(
    val ticketId: Int,
    val name: String,
    val theater: String,
    val date: String,
    val seat: String,
    val category: String,
    val file: String? = null,
)
