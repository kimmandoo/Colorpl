package com.domain.model

data class ReservationPlace(
    val reservationPlaceId: Int,
    val placeName: String,
    val theaterList: List<Theater>,
) {
    companion object {
        val DEFAULT = ReservationPlace(
            reservationPlaceId = 1,
            placeName = "수원 CGV",
            theaterList = listOf(
                Theater(
                    theaterId = 1,
                    theaterName = "1관",
                    theaterTotalSeatCount = 100,
                    timeTableList = listOf(
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        )
                    )
                ),
                Theater(
                    theaterId = 1,
                    theaterName = "2관",
                    theaterTotalSeatCount = 100,
                    timeTableList = listOf(
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        ),
                        TimeTable(
                            timeTableId = 1,
                            startTime = "10:00",
                            endTime = "",
                            remainingSeatCount = 0
                        )
                    )


                )

            )
        )
    }
}


fun List<ReservationPlace>.toModel(): List<ReservationPairInfo> {
    val list = mutableListOf<ReservationPairInfo>()
    this.forEach { reservationPlace ->
        list.addAll(reservationPlace.theaterList.map { item ->
            ReservationPairInfo(
                reservationPlaceId = reservationPlace.reservationPlaceId,
                placeName = reservationPlace.placeName,
                theaterId = item.theaterId,
                theaterName = item.theaterName,
                theaterTotalSeatCount = item.theaterTotalSeatCount,
                timeTableList = item.timeTableList
            )
        })
    }
    return list
}

