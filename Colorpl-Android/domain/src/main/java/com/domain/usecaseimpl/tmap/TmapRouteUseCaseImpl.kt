package com.domain.usecaseimpl.tmap

import com.data.repository.TmapRouteRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.Route
import com.domain.usecase.TmapRouteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class TmapRouteUseCaseImpl @Inject constructor(
    private val tmapRouteRepository: TmapRouteRepository,
) : TmapRouteUseCase {
    override suspend fun invoke(
        startX: String,
        startY: String,
        endX: String,
        endY: String
    ): Flow<Route> = flow {
        tmapRouteRepository.getRoute(startX, startY, endX, endY).collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    val route = result.data.toEntity()
                    emit(route)
                }

                is ApiResult.Error -> {
                    Timber.tag("error").e(result.exception)
                }
            }
        }
    }
}