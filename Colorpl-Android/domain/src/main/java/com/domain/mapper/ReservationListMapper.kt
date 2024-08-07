package com.domain.mapper


import com.domain.model.ReservationInfo
import timber.log.Timber

fun List<com.data.model.response.ReservationInfo>.toEntity(): List<ReservationInfo> {
    return this.map {
        ReservationInfo(
            reservationInfoId = it.id,
            contentImg = it.posterImagePath,
            title = it.name,
            category = it.category ?: "카테고리",
            runtime = it.runtime,
            price = it.priceBySeatClass.getOrDefault("R석", 10000).toString()
        )
    }
}