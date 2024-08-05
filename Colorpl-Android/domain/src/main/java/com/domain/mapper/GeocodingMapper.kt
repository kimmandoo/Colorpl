package com.domain.mapper

import com.data.model.response.ResponseGeoCoding
import com.domain.model.GeoCoordinate

fun ResponseGeoCoding.toEntity(): GeoCoordinate {
    val response = this.addresses.firstOrNull()
    return if (response != null) GeoCoordinate(response.x.toDouble(), response.y.toDouble())
    else GeoCoordinate(0.0, 0.0)
}