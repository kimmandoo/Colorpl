package com.domain.mapper

import com.data.model.response.ResponseTicketCreate
import com.domain.model.TicketCreate


fun ResponseTicketCreate.toEntity(): TicketCreate{
    return TicketCreate(
        file = file,
        name = name,
        theater = theater,
        date = date,
        seat = seat,
        category = category
    )
}