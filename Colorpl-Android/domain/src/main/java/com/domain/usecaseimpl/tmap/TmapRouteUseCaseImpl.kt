package com.domain.usecaseimpl.tmap

import com.data.repository.TmapRouteRepository
import com.domain.mapper.toEntity
import com.domain.model.Route
import com.domain.usecase.TmapRouteUseCase
import javax.inject.Inject

class TmapRouteUseCaseImpl @Inject constructor(
    private val tmapRouteRepository: TmapRouteRepository,
) : TmapRouteUseCase {
    override suspend fun getRoute(
        startX: String,
        startY: String,
        endX: String,
        endY: String,
    ): Route {
        return tmapRouteRepository.getRoute(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY
        ).toEntity()
    }

}