package com.data.datasourceimpl

import com.data.api.PayApi
import com.data.datasource.remote.PayDataSource
import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayCancel
import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult
import javax.inject.Inject

class PayDataSourceImpl @Inject constructor(
    private val payApi: PayApi
) : PayDataSource {

    override suspend fun getPaymentToken(): String {
        return payApi.getPaymentToken()
    }

    override suspend fun postPayment(
        header: String,
        receiptId: String
    ): ResponsePayResult {
        return payApi.postPayment(header, receiptId)
    }

    override suspend fun getPaymentReceipts(header: String): List<ResponsePayReceipt> {
        return payApi.getPaymentReceipts(header)
    }

    override suspend fun postPayCancel(
        header: String,
        requestPayCancel: RequestPayCancel
    ): ResponsePayCancel {
        return payApi.postPayCancel(header, requestPayCancel)
    }
}