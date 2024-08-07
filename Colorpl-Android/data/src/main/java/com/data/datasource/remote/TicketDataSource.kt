package com.data.datasource.remote

import com.data.model.response.ResponseTicket
import com.data.model.response.ResponseTicketCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TicketDataSource {
    suspend fun createTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): Int

    suspend fun getAllTicket(): List<ResponseTicket>
    suspend fun getMonthlyTicket(date: String): List<ResponseTicket>
}