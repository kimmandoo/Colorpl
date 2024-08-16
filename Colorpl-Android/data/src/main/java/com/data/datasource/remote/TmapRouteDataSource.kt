package com.data.datasource.remote

import com.data.model.response.ResponseTmapRoute

interface TmapRouteDataSource {

    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): ResponseTmapRoute
}