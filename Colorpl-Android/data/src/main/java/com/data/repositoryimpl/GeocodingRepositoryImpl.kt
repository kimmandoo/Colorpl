package com.data.repositoryimpl

import com.data.api.safeApiCall
import com.data.datasource.remote.GeocodingDataSource
import com.data.model.response.ResponseGeoCoding
import com.data.repository.GeocodingRepository
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(private val geocodingDataSource: GeocodingDataSource) :
    GeocodingRepository {
    override fun getLatLng(address: String): Flow<ApiResult<ResponseGeoCoding>> = flow {
        emit(safeApiCall {
            geocodingDataSource.getLatLng(address)
        })
    }
}