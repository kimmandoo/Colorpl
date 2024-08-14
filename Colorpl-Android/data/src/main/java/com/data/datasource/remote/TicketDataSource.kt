package com.data.datasource.remote

import com.data.model.response.ResponseSingleTicket
import com.data.model.response.ResponseTicket
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TicketDataSource {
    suspend fun createTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): Int

    suspend fun getSingleTicket(id: Int): ResponseSingleTicket
    suspend fun deleteTicket(id: Int): Unit
    suspend fun putTicket(
        id: Int,
        ticket: MultipartBody.Part?,
        request: RequestBody
    ): Int

    suspend fun getAllTicket(): List<ResponseTicket>
    suspend fun getMonthlyTicket(date: String): List<ResponseTicket>
    suspend fun createReservationTicket(
        ticket: MultipartBody.Part,
        request: RequestBody
    ): Int
}