package com.data.datasource.remote

import com.data.model.response.ResponseGeoCoding

interface GeocodingDataSource {
    suspend fun getLatLng(address: String): ResponseGeoCoding
}