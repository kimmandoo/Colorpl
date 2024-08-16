package com.data.datasource.remote

import com.data.model.response.ResponseMarker

interface NotificationDataSource {

    suspend fun getMarkers(latitude: Double, longitude: Double, radius: Double): ResponseMarker
}