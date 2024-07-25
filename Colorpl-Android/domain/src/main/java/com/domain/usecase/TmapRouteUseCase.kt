package com.domain.usecase

import com.data.util.ApiResult
import com.domain.model.Route
import com.domain.util.RepoResult
import kotlinx.coroutines.flow.Flow

interface TmapRouteUseCase {
    suspend operator fun invoke(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Flow<RepoResult<Route>>
}