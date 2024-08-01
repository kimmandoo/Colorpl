package com.domain.mapper

import com.data.model.response.ResponseTicketCreate
import com.domain.model.Ticket


fun ResponseTicketCreate.toEntity(): Ticket {
    return Ticket(
        ticketId = 1,
        file = file,
        name = name,
        theater = theater,
        date = date,
        seat = seat,
        category = category,
    )
}