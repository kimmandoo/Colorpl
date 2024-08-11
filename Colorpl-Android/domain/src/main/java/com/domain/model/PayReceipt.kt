package com.domain.model

data class PayReceipt(
    val receiptId : String,
    val orderName: String,
    val price: Int,
    val purchasedAt: String,
    val statusLocale: String
)
