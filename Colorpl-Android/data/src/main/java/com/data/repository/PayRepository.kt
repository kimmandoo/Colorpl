package com.data.repository

import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface PayRepository {

    suspend fun getPaymentToken() : Flow<ApiResult<String>>

}