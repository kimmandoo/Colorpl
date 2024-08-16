package com.domain.mapper

import com.domain.model.ReservationPairInfo

fun List<com.data.model.response.ResponseShowSchedules>.toEntity(): List<ReservationPairInfo> {
    return this.flatMap { schedule ->
        schedule.hall.map { hall ->
            ReservationPairInfo(
                placeName = schedule.name,
                hallName = hall.name,
                hallCountSeat = hall.countSeats,
                timeTableList = hall.timetable.toEntity()
            )
        }
    }
}

