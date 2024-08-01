package com.domain.model

data class TicketCreate(
    val file: String? = null,
    val name: String,
    val theater: String,
    val date: String,
    val seat: String,
    val category: String
)