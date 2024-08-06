package com.domain.mapper


import com.domain.model.ReservationInfo
import timber.log.Timber

fun com.data.model.response.ReservationInfo.toEntity(): ReservationInfo {
    Timber.d("mapper : $posterImagePath")
    return ReservationInfo(
        reservationInfoId = this.id,
        contentImg = this.posterImagePath,
        title = this.name,
        cast = this.cast,
        category = this.category,
        runtime = this.runtime,
        price = this.priceBySeatClass.gradeA.toString()
    )
}