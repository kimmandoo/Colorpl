package com.data.repositoryimpl

import com.data.datasource.NotificationDataSource
import com.data.model.response.ResponseMarkerData
import com.data.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {


    override suspend fun getMarkerData(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): ResponseMarkerData {
        return notificationDataSource.getMarkers(latitude, longitude, radius)
    }
}