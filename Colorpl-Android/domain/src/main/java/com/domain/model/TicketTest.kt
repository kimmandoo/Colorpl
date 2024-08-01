package com.domain.model

import java.util.Date

data class TicketTest(
    val ticketId: Int,
    val name: String,
    val date: Date,
    val space: String,
    val seat: String
)
