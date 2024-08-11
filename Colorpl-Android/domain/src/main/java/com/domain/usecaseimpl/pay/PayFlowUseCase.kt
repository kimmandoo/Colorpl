package com.domain.usecaseimpl.pay

import com.data.repository.PayRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.mapper.toParam
import com.domain.mapper.toPayStatus
import com.domain.model.PayCancelParam
import com.domain.model.PayReceipt
import com.domain.model.PayStatus
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PayFlowUseCase @Inject constructor(
    private val payRepository: PayRepository
) {

    suspend fun getPayToken(): Flow<DomainResult<String>> {
        return flow {
            payRepository.getPaymentToken().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun startPayment(header: String, receiptId: String): Flow<DomainResult<PayStatus>> {
        return flow {
            payRepository.postPayment(header, receiptId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data.toPayStatus()))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun getPayReceipts(header: String): Flow<DomainResult<List<PayReceipt>>> {
        return flow {
            payRepository.getPayReceipts(header).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(result.data.toEntity()))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }

            }
        }
    }

    suspend fun payCancel(
        header: String,
        payCancelParam: PayCancelParam
    ): Flow<DomainResult<Boolean>> {
        return flow {
            payRepository.postPayCancel(header, payCancelParam.toParam()).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(true))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }

    suspend fun payHistoryDelete(
        header: String,
        receiptId: String
    ): Flow<DomainResult<Boolean>> {
        return flow {
            payRepository.deletePay(header, receiptId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        emit(DomainResult.Success(true))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.Error(result.exception))
                    }
                }
            }
        }
    }
}