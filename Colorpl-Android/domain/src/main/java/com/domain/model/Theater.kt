package com.domain.model

data class Theater(
    val theaterId: Int,
    val theaterName: String,
    val theaterTotalSeatCount: Int,
    val timeTableList: List<TimeTable>,
)
