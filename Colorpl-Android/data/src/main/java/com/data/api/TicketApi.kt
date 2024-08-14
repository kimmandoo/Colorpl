package com.data.api

import com.data.model.response.ResponseSingleTicket
import com.data.model.response.ResponseTicket
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApi {

    @GET("schedules/{id}")
    suspend fun getTicket(
        @Path("id") id: Int,
    ): ResponseSingleTicket

    @DELETE("schedules/{id}")
    suspend fun deleteTicket(
        @Path("id") id: Int,
    ): Unit

    @Multipart
    @PUT("schedules/{id}")
    suspend fun putTicket(
        @Path("id") id: Int,
        @Part("request") request: RequestBody,
        @Part file: MultipartBody.Part,
    ): Unit

    @Multipart
    @POST("schedules/custom")
    suspend fun createTicket(
        @Part("request") request: RequestBody,
        @Part file: MultipartBody.Part,
    ): Int

    @Multipart
    @POST("schedules/reservation")
    suspend fun createReservationTicket(
        @Part("request") request: RequestBody,
        @Part file: MultipartBody.Part,
    ): Int

    @GET("schedules")
    suspend fun getAllTicket(): List<ResponseTicket>

    @GET("schedules/monthly")
    suspend fun getMonthlyTicket(
        @Query("date") date: String, // yyyy-MM-dd
    ): List<ResponseTicket>
}