package com.domain.usecase

import com.domain.model.Route

interface TmapRouteUseCase {
    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Route
}