package com.domain.model

import java.io.Serializable

data class PayRequest(
    val event: String,
    val receipt_id: String,
    val order_id: String,
    val price: Int,
    val tax_free: Int,
    val metadata: Metadata,
    val status: Int,
    val status_locale: String,
    val on_inapp_sdk: Boolean,
    val gateway_url: String,
    val bootpay_event: Boolean
) {
    data class Metadata(
        val showName: String,
        val showHallName: String,
        val showTheaterName: String,
        val showDetailId: Int,
        val showScheduleId: Int,
        val selectedSeatList: List<Seat>,
        val selectedDiscount: String
    ) : Serializable
}
