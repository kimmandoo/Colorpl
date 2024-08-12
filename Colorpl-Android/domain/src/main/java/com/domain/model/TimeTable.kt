package com.domain.model

data class TimeTable(
    val scheduleId: Int,
    val startTime: String,
    val endTime: String,
    val remainingSeatCount: Int,
)