package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.PayDataSource
import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayCancel
import com.data.model.response.ResponsePayHistoryDelete
import com.data.model.response.ResponsePayReceipt
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

    override suspend fun getPayReceipts(header: String): Flow<ApiResult<List<ResponsePayReceipt>>> {
        return flow {
            emit(safeApiCall {
                payDataSource.getPaymentReceipts(header)
            })
        }
    }

    override suspend fun postPayCancel(
        header: String,
        requestPayCancel: RequestPayCancel
    ): Flow<ApiResult<ResponsePayCancel>> {
        return flow {
            emit(safeApiCall {
                payDataSource.postPayCancel(header, requestPayCancel)
            })
        }
    }

    override suspend fun deletePay(
        header: String,
        receiptId: String
    ): Flow<ApiResult<ResponsePayHistoryDelete>> {
        return flow {
            emit(safeApiCall {
                payDataSource.deletePay(header, receiptId)
            })
        }
    }
}