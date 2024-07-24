package com.domain.usecase

import com.data.model.response.ResponseTmapRoute

interface TmapRouteUseCase {
    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): ResponseTmapRoute
}