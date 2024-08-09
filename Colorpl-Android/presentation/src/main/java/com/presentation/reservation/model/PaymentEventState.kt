package com.presentation.reservation.model

sealed interface PaymentEventState {

    data object PaySuccess : PaymentEventState
    data object PayFail : PaymentEventState
}