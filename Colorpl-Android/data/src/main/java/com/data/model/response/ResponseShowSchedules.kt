package com.data.model.response

data class ResponseShowSchedules (
    val name: String,
    val hall: List<Hall>,
)

data class Hall(
    val name: String,
    val countSeats: Int,
    val timetable: List<TimeTable>,
)

data class TimeTable(
    val showScheduleId: Int?,
    val startTime: String,
    val endTime: String,
    val remainingSeats: Int?,
)