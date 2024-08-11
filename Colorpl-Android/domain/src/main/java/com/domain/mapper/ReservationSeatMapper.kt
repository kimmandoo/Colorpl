package com.domain.mapper

import com.domain.model.Seat
import timber.log.Timber

fun Map<String, Boolean>.toEntity(maxRow: Int, maxCol: Int): List<Seat> {
    return (0 until maxRow).flatMap { x ->
        (0 until maxCol).map { y ->
            // 현재 row와 col에 해당하는 key를 생성
            val key = "Seat(row=$x, col=$y)"
            // 데이터에서 해당 key를 찾아서 Seat 객체 생성
            val responseSeat = this[key] ?: true

//            Seat(
//                row = responseSeat.row?. ?: row,
//                col = responseSeat?.col ?: y,
//                name = responseSeat?.name.orEmpty(),
//                grade = responseSeat?.grade.orEmpty(),
//                isReserved = responseSeat?.isReserved ?: false,
//                isSelected = false
//            )
            Seat(
                row = x,
                col = y,
                name = "${x}:${y}",
                grade = "R",
                isReserved = !responseSeat,
                isSelected = false
            )
        }
    }
}