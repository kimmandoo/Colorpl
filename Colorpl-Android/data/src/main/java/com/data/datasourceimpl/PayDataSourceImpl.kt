package com.data.datasourceimpl

import com.data.api.PayApi
import com.data.datasource.remote.PayDataSource
import javax.inject.Inject

class PayDataSourceImpl @Inject constructor(
    private val payApi: PayApi
) : PayDataSource {

    override suspend fun getPaymentToken(): String {
        return payApi.getPaymentToken()
    }
}