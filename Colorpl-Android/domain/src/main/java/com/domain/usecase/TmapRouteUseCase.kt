package com.domain.usecase

import com.domain.model.Route
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface TmapRouteUseCase {
    suspend operator fun invoke(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Flow<DomainResult<Route>>
}