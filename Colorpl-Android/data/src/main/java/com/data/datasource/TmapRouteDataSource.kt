package com.data.datasource

import com.data.model.request.RequestTmapRoute
import com.data.model.response.ResponseTmapRoute

interface TmapRouteDataSource {

    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): ResponseTmapRoute
}