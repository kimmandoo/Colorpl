package com.data.api

import com.data.model.request.RequestVision
import com.data.model.response.ResponseVision
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(@Body request: RequestVision): ResponseVision
}