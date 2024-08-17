package com.domain.mapper

import com.data.model.response.ResponseShowSeat
import com.domain.model.Seat
import timber.log.Timber

fun Map<String, ResponseShowSeat>.toEntity(maxRow: Int, maxCol: Int): List<Seat> {
    return (0 until maxRow).flatMap { x ->
        (0 until  maxCol).map { y ->
            // 현재 row와 col에 해당하는 key를 생성
            val key = "$x:$y"
            // 데이터에서 해당 key를 찾아서 Seat 객체 생성
            val responseSeat = this[key] ?: ResponseShowSeat()
            Timber.d("responseSeat : $responseSeat")

            Seat(
                row = responseSeat.row,
                col = responseSeat.col,
                name = responseSeat.name,
                grade = responseSeat.grade,
                isReserved = responseSeat.isReserved,
                isSelected = false
            )
        }
    }
}