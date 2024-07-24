package com.domain.usecaseimpl.tmap

import com.data.model.response.ResponseTmapRoute
import com.data.repository.TmapRouteRepository
import com.domain.usecase.TmapRouteUseCase
import javax.inject.Inject

class TmapRouteUseCaseImpl @Inject constructor(
    private val tmapRouteRepository: TmapRouteRepository
) : TmapRouteUseCase {
    override suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): ResponseTmapRoute {
        return tmapRouteRepository.getRoute(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY
        )
    }

}