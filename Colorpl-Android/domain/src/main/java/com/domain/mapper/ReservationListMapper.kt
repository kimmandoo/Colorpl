package com.domain.mapper


import com.domain.model.ReservationInfo
import timber.log.Timber

fun com.data.model.response.ResponseReservationShows.toEntity(): List<ReservationInfo> {
    return this.items.map {
        ReservationInfo(
            reservationInfoId = it.id,
            contentImg = it.posterImagePath,
            title = it.name,
            category = it.category,
            price = it.priceBySeatClass.gradeB.toString(),
        )
    }
}