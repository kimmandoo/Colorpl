package com.domain.usecaseimpl.naver

import com.data.repository.GeocodingRepository
import com.data.util.ApiResult
import com.domain.mapper.toEntity
import com.domain.model.GeoCoordinate
import com.domain.usecase.GeocodingUseCase
import com.domain.util.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeocodingUesCaseImpl @Inject constructor(private val geocodingRepository: GeocodingRepository) :
    GeocodingUseCase {
    override suspend fun invoke(address: String): Flow<DomainResult<GeoCoordinate>> =
        flow {
            geocodingRepository.getLatLng(address).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val latlng = result.data.toEntity()
                        emit(DomainResult.success(latlng))
                    }

                    is ApiResult.Error -> {
                        emit(DomainResult.error(result.exception))
                    }
                }
            }
        }
}