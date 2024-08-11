package com.data.model.response

data class ResponseShowSeat(
    val row: Int = -1,
    val col: Int = -1,
    val name: String = "",
    val grade: String? = null,
    val isReserved: Boolean = false,
)