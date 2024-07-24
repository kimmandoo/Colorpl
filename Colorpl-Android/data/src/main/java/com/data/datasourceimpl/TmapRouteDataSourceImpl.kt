package com.data.datasourceimpl

import com.data.api.TmapRouteApi
import com.data.datasource.TmapRouteDataSource
import com.data.model.request.RequestTmapRoute
import com.data.model.response.ResponseTmapRoute
import javax.inject.Inject

class TmapRouteDataSourceImpl @Inject constructor(
    private val tmapRouteApi: TmapRouteApi
) : TmapRouteDataSource {

    override suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): ResponseTmapRoute {
        return tmapRouteApi.getMarkers(
            RequestTmapRoute(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY,
            )
        )
    }
}