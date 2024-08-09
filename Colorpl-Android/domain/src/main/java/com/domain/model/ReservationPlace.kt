package com.domain.model

data class ReservationPlace(
    val placeName: String,
    val theaterList: List<Theater>,
) {
    companion object {
        val DEFAULT = ReservationPlace(
            placeName = "수원 CGV",
            theaterList = listOf(
                Theater(
                    theaterName = "1관",
                    theaterTotalSeatCount = 100,
                    timeTableList = listOf(
                        TimeTable(
                            scheduleId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            scheduleId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            scheduleId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            scheduleId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        )
                    )
                ),
            )
        )
    }
}


fun List<ReservationPlace>.toModel(): List<ReservationPairInfo> {
    val list = mutableListOf<ReservationPairInfo>()
    this.forEach { reservationPlace ->
        list.addAll(reservationPlace.theaterList.map { item ->
            ReservationPairInfo(
                placeName = reservationPlace.placeName,
                hallName = item.theaterName,
                hallCountSeat = item.theaterTotalSeatCount,
                timeTableList = item.timeTableList
            )
        })
    }
    return list
}

