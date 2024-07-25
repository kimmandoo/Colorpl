package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.TmapRouteDataSource
import com.data.model.response.ResponseTmapRoute
import com.data.repository.TmapRouteRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TmapRouteRepositoryImpl @Inject constructor(
    private val tmapRouteDataSource: TmapRouteDataSource
) : TmapRouteRepository {

    override suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): Flow<ApiResult<ResponseTmapRoute>> = flow {
        emit(safeApiCall {
            tmapRouteDataSource.getRoute(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY,
            )
        })
    }
}