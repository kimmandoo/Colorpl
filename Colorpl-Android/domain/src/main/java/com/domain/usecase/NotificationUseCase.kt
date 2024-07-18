package com.domain.usecase

import com.domain.model.MarkerData

interface NotificationUseCase {

    suspend fun getMarkers(latitude : Double, longitude : Double, radius : Double) : List<MarkerData>
}