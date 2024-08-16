package com.data.repositoryimpl

import com.data.datasource.remote.NotificationDataSource
import com.data.model.response.ResponseMarker
import com.data.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource

) : NotificationRepository {


    override suspend fun getMarkerData(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): ResponseMarker {
        return notificationDataSource.getMarkers(latitude, longitude, radius)
    }
}