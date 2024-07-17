package com.data.datasource

import com.data.model.notification.ResponseMarkerData

interface NotificationDataSource {

    suspend fun getMarkers(latitude : Double, longitude : Double, radius : Double) : ResponseMarkerData
}