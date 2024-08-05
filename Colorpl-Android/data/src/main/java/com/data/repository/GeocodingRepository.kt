package com.data.repository

import com.data.model.response.ResponseGeoCoding
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface GeocodingRepository {
    fun getLatLng(address: String): Flow<ApiResult<ResponseGeoCoding>>
}