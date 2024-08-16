package com.domain.usecase

import com.domain.model.GeoCoordinate
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface GeocodingUseCase {
    suspend operator fun invoke(address: String): Flow<DomainResult<GeoCoordinate>>
}