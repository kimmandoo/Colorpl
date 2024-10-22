package com.domain.mapper

import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseSingleTicket
import com.data.model.response.ResponseTicket
import com.data.model.response.ResponseTicketCreate
import com.domain.model.TicketRequest
import com.domain.model.TicketResponse


fun ResponseTicketCreate.toEntity(): Int {
    return this.ticketId
}

fun ResponseTicket.toEntity(): TicketResponse {
    return TicketResponse(
        id = id,
        seat = seat,
        dateTime = dateTime,
        name = name,
        category = category,
        location = location,
        latitude = latitude,
        longitude = longitude,
        imgUrl = imgUrl,
        reviewExists = reviewExists,
        reviewId = reviewId
    )
}

fun ResponseSingleTicket.toEntity(id: Int): TicketResponse {
    return TicketResponse(
        id = id,
        seat = seat,
        dateTime = dateTime,
        name = name,
        category = category,
        location = location,
        latitude = latitude,
        longitude = longitude,
        imgUrl = image,
        reviewExists = reviewExists,
        reviewId = reviewId,
        type = type
    )
}

fun TicketRequest.toEntity(): RequestTicketCreate {
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