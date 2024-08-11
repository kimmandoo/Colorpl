package com.data.repository

import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayCancel
import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface PayRepository {

    suspend fun getPaymentToken(): Flow<ApiResult<String>>

    suspend fun postPayment(
        header: String,
        receiptId: String
    ): Flow<ApiResult<ResponsePayResult>>

    suspend fun getPayReceipts(
        header: String
    ): Flow<ApiResult<List<ResponsePayReceipt>>>

    suspend fun postPayCancel(
        header: String,
        requestPayCancel: RequestPayCancel
    ): Flow<ApiResult<ResponsePayCancel>>
}