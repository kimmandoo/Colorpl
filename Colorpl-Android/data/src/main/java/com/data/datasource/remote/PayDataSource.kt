package com.data.datasource.remote

import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayCancel
import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult

interface PayDataSource {

    suspend fun getPaymentToken(): String

    suspend fun postPayment(header: String, receiptId: String): ResponsePayResult

    suspend fun getPaymentReceipts(header: String): List<ResponsePayReceipt>

    suspend fun postPayCancel(header: String, requestPayCancel: RequestPayCancel): ResponsePayCancel
}