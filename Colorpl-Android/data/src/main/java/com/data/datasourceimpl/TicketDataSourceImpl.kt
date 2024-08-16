package com.data.datasourceimpl

import com.data.api.TicketApi
import com.data.datasource.remote.TicketDataSource
import com.data.model.response.ResponseSingleTicket
import com.data.model.response.ResponseTicket
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class TicketDataSourceImpl @Inject constructor(
    private val api: TicketApi
) : TicketDataSource {
    override suspend fun createTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): Int {
        return api.createTicket(
            request = request,
            file = ticket
        )
    }

    override suspend fun getSingleTicket(id: Int): ResponseSingleTicket {
        return api.getTicket(id)
    }

    override suspend fun deleteTicket(id: Int) {
        return api.deleteTicket(id)
    }

    override suspend fun putTicket(
        id: Int,
        ticket: MultipartBody.Part?,
        request: RequestBody
    ): Int {
        return api.putTicket(id, request, ticket)
    }

    override suspend fun getAllTicket(): List<ResponseTicket> {
        return api.getAllTicket()
    }

    override suspend fun getMonthlyTicket(date: String): List<ResponseTicket> {
        return api.getMonthlyTicket(date)
    }

    override suspend fun createReservationTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): Int {
        return api.createReservationTicket(
            request = request,
            file = ticket
        )
    }
}