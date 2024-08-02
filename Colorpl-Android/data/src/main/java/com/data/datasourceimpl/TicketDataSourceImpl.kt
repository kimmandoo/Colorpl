package com.data.datasourceimpl

import com.data.api.TicketApi
import com.data.datasource.remote.TicketDataSource
import com.data.model.response.ResponseTicketCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class TicketDataSourceImpl @Inject constructor(
    private val api: TicketApi
) : TicketDataSource {
    override suspend fun createTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): ResponseTicketCreate {
        return api.createTicket(
            request = request,
            file = ticket
        )
    }
}