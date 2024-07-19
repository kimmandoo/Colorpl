package com.domain.usecaseimpl.notification

import com.data.repository.NotificationRepository
import com.domain.mapper.toEntity
import com.domain.model.Marker
import com.domain.usecase.NotificationUseCase
import javax.inject.Inject

class NotificationUseCaseImpl @Inject constructor(
    private val notificationRepository: NotificationRepository
) : NotificationUseCase{

    override suspend fun getMarkers(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Marker> {
        return notificationRepository.getMarkerData(latitude, longitude, radius).toEntity()
    }
}