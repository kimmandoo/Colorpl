package com.data.repository

import com.data.model.notification.ResponseMarkerData

interface NotificationRepository {

    suspend fun getMarkers(latitude : Double, longitude : Double, radius : Double) : ResponseMarkerData
}