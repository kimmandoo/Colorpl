package com.domain.mapper

import com.domain.model.TimeTable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun List<com.data.model.response.TimeTable>.toEntity(): List<TimeTable> {
    return this.map {
        TimeTable(
            scheduleId = it.scheduleId?: 1,
            startTime = it.startTime.toHHMMparser(),
            endTime = it.endTime.toHHMMparser(),
            remainingSeatCount = it.remainingSeats?: 100,
        )
    }
}

// String 확장 함수 정의
fun String.toHHMMparser(): String {
    val dateTime = LocalDateTime.parse(this)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return dateTime.format(timeFormatter)
}