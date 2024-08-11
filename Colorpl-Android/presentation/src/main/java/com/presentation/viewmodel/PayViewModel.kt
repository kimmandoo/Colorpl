package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.PayCancelParam
import com.domain.model.PayReceipt
import com.domain.usecaseimpl.pay.PayFlowUseCase
import com.domain.util.DomainResult
import com.presentation.reservation.model.PaymentEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val payFlowUseCase: PayFlowUseCase
) : ViewModel() {


    private val _payToken = MutableStateFlow<String>("")
    val payToken: StateFlow<String> get() = _payToken

    fun setPayToken(value: String) {
        _payToken.value = value
    }

    init {
        getPayToken()
    }

    fun getPayToken() {
        viewModelScope.launch {
            payFlowUseCase.getPayToken().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        Timber.d("페이 토큰 가져오기 성공 ${result.data}")
                        setPayToken(result.data)
                    }

                    is DomainResult.Error -> {
                        Timber.d("액세스 토큰 가져오기 실패! ${result.exception}")
                    }
                }
            }
        }
    }

    private val _paymentEventState = MutableSharedFlow<PaymentEventState>()
    val paymentEventState: SharedFlow<PaymentEventState> get() = _paymentEventState

    fun startPayment(receiptId: String) {
        viewModelScope.launch {
            payFlowUseCase.startPayment(payToken.value, receiptId).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _paymentEventState.emit(PaymentEventState.PaySuccess)
                    }

                    is DomainResult.Error -> {
                        _paymentEventState.emit(PaymentEventState.PayFail)
                        Timber.d("결제 에러 ${result.exception}")
                    }

                }
            }
        }
    }

    private val _paymentReceipts = MutableStateFlow<List<PayReceipt>>(listOf())
    val paymentReceipts: StateFlow<List<PayReceipt>> get() = _paymentReceipts

    fun setPaymentReceipts(value: List<PayReceipt>) {
        _paymentReceipts.value = value
    }

    fun getPaymentReceipts() {
        viewModelScope.launch {
            payFlowUseCase.getPayReceipts(payToken.value).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        setPaymentReceipts(result.data)
                    }

                    is DomainResult.Error -> {
                        Timber.d("결제 내역 불러오기 실패 ${result.exception}")
                    }
                }
            }
        }
    }

    fun payCancel(receiptId: String) {
        viewModelScope.launch {
            payFlowUseCase.payCancel(payToken.value, PayCancelParam(receiptId))
                .collectLatest { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            getPaymentReceipts()
                        }

                        is DomainResult.Error -> {
                            Timber.d("결제 취소 ${result.exception}")
                        }
                    }
                }
        }
    }

    fun payHistoryDelete(receiptId: String) {
        viewModelScope.launch {
            payFlowUseCase.payHistoryDelete(payToken.value, receiptId)
                .collectLatest { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            getPaymentReceipts()
                        }

                        is DomainResult.Error -> {
                            Timber.d("결제 내역 삭제 ${result.exception}")
                        }
                    }
                }
        }
    }
}