package com.data.repository

import com.data.model.response.ResponseMarker

interface NotificationRepository {


    suspend fun getMarkerData(
        latitude : Double, longitude : Double, radius : Double
    ) : ResponseMarker
}