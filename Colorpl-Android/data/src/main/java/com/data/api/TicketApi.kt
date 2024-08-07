package com.data.api

import com.data.model.response.ResponseTicket
import com.data.model.response.ResponseTicketCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface TicketApi {
    @Multipart
    @POST("schedules/custom")
    suspend fun createTicket(
        @Part("request") request: RequestBody,
        @Part file: MultipartBody.Part,
    ): Int

    @GET("schedules/")
    suspend fun getAllTicket(): List<ResponseTicket>

    @GET("schedules/monthly")
    suspend fun getMonthlyTicket(
        @Query("date") date: String, // yyyy-MM-dd
    ): List<ResponseTicket>
}