package com.domain.model

data class ReservationPayInfo(
    val amountOfBefore: Int = 0,
    val amountOfDiscount: Int = 0,
) {
    val amountOfAfter: Int
        get() = amountOfBefore - amountOfDiscount
}
