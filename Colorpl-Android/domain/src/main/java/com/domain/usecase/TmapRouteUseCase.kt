package com.domain.usecase

import com.data.util.ApiResult
import com.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface TmapRouteUseCase {
    suspend operator fun invoke(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Flow<Route>
}