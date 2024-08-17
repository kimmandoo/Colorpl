package com.domain.mapper


import com.data.model.paging.reservation.Show
import com.domain.model.ReservationInfo

fun List<Show>.toEntity(): List<ReservationInfo> {
    return this.map {
        ReservationInfo(
            reservationInfoId = it.id,
            contentImg = it.posterImagePath,
            title = it.name,
            category = it.category ?: "카테고리",
            runtime = it.runtime,
            priceBySeatClass = it.priceBySeatClass
        )
    }
}