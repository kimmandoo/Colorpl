package com.domain.model

data class PayResult(
    val purchaseAt : String? = "",
    val scheduleId : Int = 0,
    val receiptId : String ?= "",
    val httpStatus : Int = 0,
    val price : Int = 0
)
