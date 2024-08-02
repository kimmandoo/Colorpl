package com.domain.mapper

import com.data.model.response.ResponseTicketCreate


fun ResponseTicketCreate.toEntity(): Int {
    return this.ticketId
}