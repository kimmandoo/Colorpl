package com.data.datasourceimpl

import com.data.api.NotificationApi
import com.data.datasource.remote.NotificationDataSource
import com.data.model.response.ResponseMarker
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationApi : NotificationApi
) : NotificationDataSource {

    override suspend fun getMarkers(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): ResponseMarker {
        return notificationApi.getMarkers(latitude, longitude, radius)
    }
}