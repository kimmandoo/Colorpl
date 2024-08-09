package com.data.model.response

data class ResponseShowSchedules (
    val name: String,
    val hall: List<Hall>,
)

data class Hall(
    val name: String,
    val countSeat: Int,
    val timeTable: List<TimeTable>,
)

data class TimeTable(
    val scheduleId: Int,
    val startTime: String,
    val endTime: String,
    val remainingSeatCount: Int,
)