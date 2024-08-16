package com.data.datasourceimpl

import com.data.api.NaverApi
import com.data.datasource.remote.GeocodingDataSource
import com.data.model.response.ResponseGeoCoding
import javax.inject.Inject

class GeocodingDataSourceImpl @Inject constructor(private val naverApi: NaverApi) :
    GeocodingDataSource {
    override suspend fun getLatLng(address: String): ResponseGeoCoding {
        return naverApi.getLatLng(address)
    }
}