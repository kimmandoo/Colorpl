package com.data.repositoryimpl

import com.data.datasource.TmapRouteDataSource
import com.data.model.response.ResponseTmapRoute
import com.data.repository.TmapRouteRepository
import javax.inject.Inject

class TmapRouteRepositoryImpl @Inject constructor(
    private val tmapRouteDataSource: TmapRouteDataSource
) : TmapRouteRepository {

    override suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): ResponseTmapRoute {
        return tmapRouteDataSource.getRoute(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
        )
    }
}