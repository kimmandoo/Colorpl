package com.data.datasourceimpl

import com.data.api.PayApi
import com.data.datasource.remote.PayDataSource
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
}