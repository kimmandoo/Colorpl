package com.domain.mapper

import com.data.model.response.ResponseMarkerData
import com.domain.model.MarkerData

fun ResponseMarkerData.toEntity() : List<MarkerData>{
    return this.data.map {
        MarkerData(
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}