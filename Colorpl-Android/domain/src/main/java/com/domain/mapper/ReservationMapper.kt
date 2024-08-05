package com.domain.mapper


import com.domain.model.ReservationInfo
import timber.log.Timber

fun com.data.model.response.ReservationInfo.toEntity(): ReservationInfo {
    Timber.d("mapper : $posterImagePath")
    return ReservationInfo(
        reservationInfoId = this.id,
        contentImg = this.posterImagePath,
        title = this.name,
        category = "연극",
        runtime = this.runtime,
        price = "10,000"
    )
}