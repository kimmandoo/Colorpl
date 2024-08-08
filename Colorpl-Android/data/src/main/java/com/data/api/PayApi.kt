package com.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PayApi {

    @GET("api/payment/token")
    suspend fun getPaymentToken() : String


    @POST("api/payment/confirm")
    suspend fun postPayment(
        @Header("Authorization") Authorization : String,
        @Query("receiptId") receiptId : String
    ) : 
}