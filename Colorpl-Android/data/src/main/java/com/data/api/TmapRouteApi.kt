package com.data.api

import com.data.model.request.RequestTmapRoute
import com.data.model.response.ResponseTmapRoute
import retrofit2.http.Body
import retrofit2.http.POST

interface TmapRouteApi {
    @POST("transit/routes")
    suspend fun getMarkers(
        @Body requestTmapRoute: RequestTmapRoute
    ): ResponseTmapRoute
}