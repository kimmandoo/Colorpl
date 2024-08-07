package com.domain.mapper

import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseTicket
import com.data.model.response.ResponseTicketCreate
import com.domain.model.Ticket


fun ResponseTicketCreate.toEntity(): Int {
    return this.ticketId
}

fun ResponseTicket.toEntity(): Ticket {
    return Ticket(
        name = name,
        location = location,
        dateTime = dateTime,
        seat = seat,
        category = category,
        latitude = latitude,
        longitude = longitude
    )
}

fun Ticket.toEntity(): RequestTicketCreate {
    return RequestTicketCreate(
        category = category,
        name = name,
        dateTime = dateTime,
        location = location,
        seat = seat,
        latitude = latitude,
        longitude = longitude
    )
}