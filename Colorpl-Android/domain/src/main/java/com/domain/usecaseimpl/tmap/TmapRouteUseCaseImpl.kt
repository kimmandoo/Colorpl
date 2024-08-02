package com.domain.usecaseimpl.tmap

import com.data.repository.TmapRouteRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Route
import com.domain.usecase.TmapRouteUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TmapRouteUseCaseImpl @Inject constructor(
    private val tmapRouteRepository: TmapRouteRepository,
) : TmapRouteUseCase {
    override suspend operator fun invoke(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Flow<DomainResult<Route>> = flow {
        tmapRouteRepository.getRoute(startX, startY, endX, endY).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val route = result.data.toEntity()
                    emit(DomainResult.success(route))
                }

                is ApiResult.Error -> {
                    emit(DomainResult.error(result.exception))
                }
            }
        }
    }
}