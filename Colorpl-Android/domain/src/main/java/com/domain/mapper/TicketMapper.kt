package com.domain.mapper

import com.data.model.response.ResponseTicketCreate


fun ResponseTicketCreate.toEntity(): String {
    return this.file
}