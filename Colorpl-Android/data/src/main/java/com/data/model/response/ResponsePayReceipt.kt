package com.data.model.response

import java.util.Date

data class ResponsePayReceipt(
    val receiptId : String,
    val orderName: String,
    val price: Int,
    val purchasedAt: String,
    val statusLocale: String,
    val showDateTime : Date,
    val showDetailPosterImagePath : String,
    val seatInfoDto : List<SeatInfoDto>
){
    data class SeatInfoDto(
        val row : Int,
        val col : Int
    )
}