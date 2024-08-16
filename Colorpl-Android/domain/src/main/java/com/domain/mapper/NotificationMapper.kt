package com.domain.mapper

import com.data.model.response.ResponseMarker
import com.domain.model.Marker

fun ResponseMarker.toEntity() : List<Marker>{
    return this.data.map {
        Marker(
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}