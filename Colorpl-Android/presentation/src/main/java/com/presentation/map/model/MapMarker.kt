package com.presentation.map.model

import com.domain.model.TicketResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

data class MapMarker(
    val id: Int,
    val seat: String,
    val dateTime: String,
    val name: String,
    val category: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val imgUrl: String,
    val reviewExists: Boolean,
    val reviewId: Int
) : ClusteringKey{
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    fun toTicketResponse(): TicketResponse {
        return TicketResponse(
            id = id,
            seat = seat,
            dateTime = dateTime,
            name = name,
            category = category,
            location = location,
            latitude = latitude,
            longitude = longitude,
            imgUrl = imgUrl,
            reviewExists = reviewExists,
            reviewId = reviewId
        )
    }
}


