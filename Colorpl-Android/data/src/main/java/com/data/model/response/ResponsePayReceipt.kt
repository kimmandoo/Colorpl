package com.data.model.response

import java.util.Date

data class ResponsePayReceipt(
    val receiptId : String,
    val orderName: String,
    val price: Int,
    val purchasedAt: String,
    val statusLocale: String,
    val showDateTime : String,
    val showDetailPosterImagePath : String,
    val theaterName : String,
    val seats : List<Seats>
){
    data class Seats(
        val row : Int,
        val col : Int,
        val name : String,
        val grade : String
    )
}