package com.domain.model

import java.util.Date

data class Ticket(
    val ticketId: Int,
    val name: String,
    val date: Date,
    val space: String,
    val seat: String
)
