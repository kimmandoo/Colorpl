package com.domain.mapper

import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult
import com.domain.model.PayReceipt
import com.domain.model.PayStatus

fun ResponsePayResult.toPayStatus(): PayStatus {
    return PayStatus(
        status = this.httpStatus == 200
    )
}

fun List<ResponsePayReceipt>.toEntity(): List<PayReceipt> {
    return this.map {
        PayReceipt(
            orderName = it.orderName,
            price = it.price,
            purchasedAt = it.purchasedAt,
            statusLocale = it.statusLocale
        )
    }
}