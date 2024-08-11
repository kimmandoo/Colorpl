package com.data.model.response

data class ResponseShowSeat(
    val data: Map<String, Boolean>
)

data class ResponseSeat(
    val row: Int,
    val col: Int,
    val name: String,
    val grade: String,
    val isReserved: Boolean,
)