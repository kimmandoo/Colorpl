package com.data.repository

import com.data.model.response.ResponseTmapRoute

interface TmapRouteRepository {
    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ) : ResponseTmapRoute
}