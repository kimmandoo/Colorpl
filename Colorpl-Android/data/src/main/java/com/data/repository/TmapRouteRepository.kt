package com.data.repository

import com.data.model.response.ResponseTmapRoute
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface TmapRouteRepository {
    suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ) : Flow<ApiResult<ResponseTmapRoute>>
}