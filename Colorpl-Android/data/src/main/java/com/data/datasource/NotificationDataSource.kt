package com.data.datasource

import com.data.model.response.ResponseMarker

interface NotificationDataSource {

    suspend fun getMarkers(latitude : Double, longitude : Double, radius : Double) : ResponseMarker
}