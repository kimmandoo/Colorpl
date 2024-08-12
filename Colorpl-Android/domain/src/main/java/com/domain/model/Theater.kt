package com.domain.model

data class Theater(
    val theaterName: String,
    val theaterTotalSeatCount: Int,
    val timeTableList: List<TimeTable>,
)
