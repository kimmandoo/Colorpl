package com.data.datasourcimpl

import com.data.api.NotificationApi
import com.data.datasource.NotificationDataSource
import com.data.model.notification.ResponseMarkerData
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationApi: NotificationApi
) : NotificationDataSource {

    override suspend fun getMarkers(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): ResponseMarkerData {
        return notificationApi.getMarkers(latitude, longitude, radius)
    }
}