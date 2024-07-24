package com.domain.usecase

import TmapRoute

interface TmapRouteUseCase {
    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): TmapRoute
}