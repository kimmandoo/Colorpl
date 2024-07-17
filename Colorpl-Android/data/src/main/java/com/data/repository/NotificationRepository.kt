package com.data.repository

import com.data.model.response.ResponseMarkerData

interface NotificationRepository {


    suspend fun getMarkerData(
        latitude : Double, longitude : Double, radius : Double
    ) : ResponseMarkerData
}