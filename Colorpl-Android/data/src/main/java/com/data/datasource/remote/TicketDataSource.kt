package com.data.datasource.remote

import com.data.model.request.RequestTicketCreate
import com.data.model.response.ResponseTicketCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TicketDataSource {
    suspend fun createTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): ResponseTicketCreate
}