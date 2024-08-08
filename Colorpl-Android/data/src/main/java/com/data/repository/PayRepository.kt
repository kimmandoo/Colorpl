package com.data.repository

import com.data.model.response.ResponsePayResult
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface PayRepository {

    suspend fun getPaymentToken() : Flow<ApiResult<String>>

    suspend fun postPayment(
        header: String,
        receiptId: String
    ): Flow<ApiResult<ResponsePayResult>>
}