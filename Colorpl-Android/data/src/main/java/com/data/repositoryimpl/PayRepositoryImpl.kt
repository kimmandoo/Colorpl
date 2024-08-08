package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.PayDataSource
import com.data.model.response.ResponsePayResult
import com.data.repository.PayRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PayRepositoryImpl @Inject constructor(
    private val payDataSource: PayDataSource
) : PayRepository {

    override suspend fun getPaymentToken(): Flow<ApiResult<String>> {
        return flow {
            emit(safeApiCall {
                payDataSource.getPaymentToken()
            })
        }
    }

    override suspend fun postPayment(
        header: String,
        receiptId: String
    ): Flow<ApiResult<ResponsePayResult>> {
        return flow {
            emit(safeApiCall {
                payDataSource.postPayment(header, receiptId)
            })
        }
    }
}