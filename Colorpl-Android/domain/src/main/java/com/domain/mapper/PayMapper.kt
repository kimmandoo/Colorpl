package com.domain.mapper

import com.data.model.response.ResponsePayResult
import com.domain.model.PayStatus

fun ResponsePayResult.toPayStatus(): PayStatus {
    return PayStatus(
        status = this.httpStatus == 200
    )
}