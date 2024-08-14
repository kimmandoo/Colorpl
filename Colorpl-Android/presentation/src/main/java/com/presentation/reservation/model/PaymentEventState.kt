package com.presentation.reservation.model

import com.domain.model.PayResult

sealed interface PaymentEventState {

    data class PaySuccess(val data : PayResult) : PaymentEventState
    data object PayFail : PaymentEventState
}