package com.domain.mapper

import com.data.model.request.RequestPayCancel
import com.data.model.response.ResponsePayReceipt
import com.data.model.response.ResponsePayResult
import com.domain.model.PayCancelParam
import com.domain.model.PayReceipt
import com.domain.model.PayResult
import com.domain.model.Seat

fun ResponsePayResult.toPayResult(): PayResult {
    return PayResult(
        purchaseAt = this.purchased_at ,
        scheduleId = this.scheduleId,
        receiptId = this.receipt_id,
        httpStatus = this.http_status,
        price = this.price
    )
}

fun List<ResponsePayReceipt>.toEntity(): List<PayReceipt> {
    return this.map {
        PayReceipt(
            receiptId = it.receiptId,
            orderName = it.orderName,
            price = it.price,
            purchasedAt = it.purchasedAt,
            statusLocale = it.statusLocale,
            showDateTime = it.showDateTime,
            showDetailPosterImagePath = it.showDetailPosterImagePath,
            theaterName = it.theaterName,
            seatInfoDto = it.seats.map {
                PayReceipt.Seats(
                    row = it.row,
                    col = it.col,
                    name = it.name,
                    grade = it.grade
                )
            }
        )
    }
}

fun PayCancelParam.toParam(): RequestPayCancel {
    return RequestPayCancel(
        receiptId = this.receiptId
    )
}

fun List<PayReceipt.Seats>.toSeatList(): List<Seat> {
    return this.map {
        Seat(
            row = it.row,
            col = it.col,
            name = it.name,
            grade = it.grade
        )
    }
}