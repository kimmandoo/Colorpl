package com.domain.mapper

import com.domain.model.TimeTable

fun List<com.data.model.response.TimeTable>.toEntity(): List<TimeTable> {
    return this.map {
        TimeTable(
            scheduleId = it.scheduleId,
            startTime = it.startTime,
            endTime = it.endTime,
            remainingSeatCount = it.remainingSeatCount,
        )
    }
}