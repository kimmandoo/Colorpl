package com.data.api

import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayCancel
import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PayApi {

    @GET("api/payment/token")
    suspend fun getPaymentToken(): String


    @POST("api/payment/confirm")
    suspend fun postPayment(
        @Header("Pay-Authorization") payAuthorization: String,
        @Query("receiptId") receiptId: String
    ): ResponsePayResult

    @GET("api/payment/receipts")
    suspend fun getPaymentReceipts(
        @Header("Pay-Authorization") payAuthorization: String
    ): List<ResponsePayReceipt>

    @POST("api/payment/cancel")
    suspend fun postPayCancel(
        @Header("Pay-Authorization") payAuthorization: String,
        @Body requestPayCancel: RequestPayCancel
    ): ResponsePayCancel

}