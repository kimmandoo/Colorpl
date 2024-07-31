package com.domain.model

data class TimeTable(
    val timeTableId: Int,
    val startTime: String,
    val endTime: String,
    val remainingSeatCount: Int,
)