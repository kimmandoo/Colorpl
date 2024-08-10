package com.data.model.response

data class ResponsePayReceipt(
    val orderName: String,
    val price: Int,
    val purchasedAt: String,
    val statusLocale: String
)