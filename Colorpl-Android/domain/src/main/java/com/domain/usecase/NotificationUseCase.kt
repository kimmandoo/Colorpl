package com.domain.usecase

import com.domain.model.Marker

interface NotificationUseCase {

    suspend fun getMarkers(latitude : Double, longitude : Double, radius : Double) : List<Marker>
}