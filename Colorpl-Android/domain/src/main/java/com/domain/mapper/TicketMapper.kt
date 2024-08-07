package com.domain.mapper

import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseTicketCreate
import com.domain.model.Ticket


fun ResponseTicketCreate.toEntity(): Int {
    return this.ticketId
}

fun Ticket.toEntity(): RequestTicketCreate {
    return RequestTicketCreate(
        category = category,
        name = name,
        dateTime = date,
        location = location,
        seat = seat,
        latitude = latitude,
        longitude = longitude
    )
}